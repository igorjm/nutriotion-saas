ALTER TABLE care_relationship
    ADD CONSTRAINT ux_care_relationship_organization_id UNIQUE (organization_id, id);

CREATE TABLE patient_intake_record (
    id UUID PRIMARY KEY,
    organization_id UUID NOT NULL REFERENCES organization(id),
    care_relationship_id UUID NOT NULL,
    updated_by_user_id UUID NOT NULL REFERENCES app_user(id),
    allergies TEXT NOT NULL DEFAULT '',
    food_restrictions TEXT NOT NULL DEFAULT '',
    clinical_history TEXT NOT NULL DEFAULT '',
    routine_notes TEXT NOT NULL DEFAULT '',
    care_goal TEXT NOT NULL DEFAULT '',
    version INTEGER NOT NULL DEFAULT 1 CHECK (version > 0),
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_patient_intake_relationship
        FOREIGN KEY (organization_id, care_relationship_id)
        REFERENCES care_relationship (organization_id, id),
    UNIQUE (organization_id, care_relationship_id)
);

CREATE INDEX ix_patient_intake_updated_by ON patient_intake_record (updated_by_user_id);

CREATE TABLE consultation (
    id UUID PRIMARY KEY,
    organization_id UUID NOT NULL REFERENCES organization(id),
    care_relationship_id UUID NOT NULL,
    created_by_user_id UUID NOT NULL REFERENCES app_user(id),
    status TEXT NOT NULL CHECK (status IN ('IN_PROGRESS', 'FINALIZED')),
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    finalized_at TIMESTAMPTZ NULL,
    CONSTRAINT fk_consultation_relationship
        FOREIGN KEY (organization_id, care_relationship_id)
        REFERENCES care_relationship (organization_id, id),
    CONSTRAINT ck_consultation_finalization
        CHECK (
            (status = 'IN_PROGRESS' AND finalized_at IS NULL)
            OR (status = 'FINALIZED' AND finalized_at IS NOT NULL)
        ),
    UNIQUE (organization_id, id)
);

CREATE UNIQUE INDEX ux_consultation_in_progress
    ON consultation (organization_id, care_relationship_id)
    WHERE status = 'IN_PROGRESS';
CREATE INDEX ix_consultation_relationship_time
    ON consultation (organization_id, care_relationship_id, created_at DESC);
CREATE INDEX ix_consultation_created_by ON consultation (created_by_user_id);

CREATE TABLE clinical_note_version (
    id UUID PRIMARY KEY,
    organization_id UUID NOT NULL REFERENCES organization(id),
    consultation_id UUID NOT NULL,
    version INTEGER NOT NULL CHECK (version > 0),
    status TEXT NOT NULL CHECK (status IN ('DRAFT', 'FINALIZED')),
    subjective TEXT NOT NULL DEFAULT '',
    objective TEXT NOT NULL DEFAULT '',
    assessment TEXT NOT NULL DEFAULT '',
    agreed_actions TEXT NOT NULL DEFAULT '',
    author_user_id UUID NOT NULL REFERENCES app_user(id),
    amends_note_version_id UUID NULL,
    amendment_reason TEXT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    finalized_at TIMESTAMPTZ NULL,
    CONSTRAINT fk_clinical_note_consultation
        FOREIGN KEY (organization_id, consultation_id)
        REFERENCES consultation (organization_id, id),
    CONSTRAINT ck_clinical_note_finalization
        CHECK (
            (status = 'DRAFT' AND finalized_at IS NULL)
            OR (status = 'FINALIZED' AND finalized_at IS NOT NULL)
        ),
    CONSTRAINT ck_clinical_note_amendment
        CHECK (
            (amends_note_version_id IS NULL AND amendment_reason IS NULL)
            OR (
                amends_note_version_id IS NOT NULL
                AND length(trim(amendment_reason)) >= 5
            )
        ),
    UNIQUE (organization_id, consultation_id, version),
    UNIQUE (organization_id, id)
);

ALTER TABLE clinical_note_version
    ADD CONSTRAINT fk_clinical_note_amended_version
    FOREIGN KEY (organization_id, amends_note_version_id)
    REFERENCES clinical_note_version (organization_id, id);

CREATE INDEX ix_clinical_note_consultation_version
    ON clinical_note_version (organization_id, consultation_id, version DESC);
CREATE INDEX ix_clinical_note_author ON clinical_note_version (author_user_id);
CREATE INDEX ix_clinical_note_amended_version ON clinical_note_version (amends_note_version_id)
    WHERE amends_note_version_id IS NOT NULL;

CREATE OR REPLACE FUNCTION prevent_finalized_clinical_note_mutation()
RETURNS TRIGGER AS $$
BEGIN
    IF OLD.status = 'FINALIZED' THEN
        RAISE EXCEPTION 'finalized clinical notes are immutable';
    END IF;
    IF TG_OP = 'DELETE' THEN
        RETURN OLD;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER clinical_note_finalized_no_mutation
BEFORE UPDATE OR DELETE ON clinical_note_version
FOR EACH ROW EXECUTE FUNCTION prevent_finalized_clinical_note_mutation();

ALTER FUNCTION prevent_finalized_clinical_note_mutation() SET search_path = pg_catalog;

ALTER TABLE patient_intake_record ENABLE ROW LEVEL SECURITY;
ALTER TABLE consultation ENABLE ROW LEVEL SECURITY;
ALTER TABLE clinical_note_version ENABLE ROW LEVEL SECURITY;

REVOKE ALL ON patient_intake_record FROM PUBLIC;
REVOKE ALL ON consultation FROM PUBLIC;
REVOKE ALL ON clinical_note_version FROM PUBLIC;
REVOKE ALL ON FUNCTION prevent_finalized_clinical_note_mutation() FROM PUBLIC;

DO $$
DECLARE
    api_role TEXT;
BEGIN
    FOREACH api_role IN ARRAY ARRAY['anon', 'authenticated']
    LOOP
        IF EXISTS (SELECT 1 FROM pg_roles WHERE rolname = api_role) THEN
            EXECUTE format('REVOKE ALL ON patient_intake_record FROM %I', api_role);
            EXECUTE format('REVOKE ALL ON consultation FROM %I', api_role);
            EXECUTE format('REVOKE ALL ON clinical_note_version FROM %I', api_role);
            EXECUTE format(
                'REVOKE ALL ON FUNCTION prevent_finalized_clinical_note_mutation() FROM %I',
                api_role
            );
        END IF;
    END LOOP;
END;
$$;
