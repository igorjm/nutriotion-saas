ALTER TABLE patient_person
    ADD COLUMN user_id UUID NULL REFERENCES app_user(id),
    ADD COLUMN care_focus VARCHAR(120) NULL;

CREATE UNIQUE INDEX ux_patient_person_user
    ON patient_person (user_id)
    WHERE user_id IS NOT NULL;

CREATE TABLE patient_invitation (
    id UUID PRIMARY KEY,
    organization_id UUID NOT NULL REFERENCES organization(id),
    patient_person_id UUID NOT NULL REFERENCES patient_person(id),
    invited_by_user_id UUID NOT NULL REFERENCES app_user(id),
    accepted_by_user_id UUID NULL REFERENCES app_user(id),
    email VARCHAR(254) NOT NULL,
    token_hash CHAR(64) NOT NULL UNIQUE,
    status VARCHAR(20) NOT NULL CHECK (status IN ('PENDING', 'ACCEPTED', 'REVOKED', 'EXPIRED')),
    expires_at TIMESTAMPTZ NOT NULL,
    accepted_at TIMESTAMPTZ NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CHECK (
        (status = 'ACCEPTED' AND accepted_by_user_id IS NOT NULL AND accepted_at IS NOT NULL)
        OR
        (status <> 'ACCEPTED' AND accepted_by_user_id IS NULL AND accepted_at IS NULL)
    )
);

CREATE UNIQUE INDEX ux_patient_invitation_pending_email
    ON patient_invitation (organization_id, lower(email))
    WHERE status = 'PENDING';
CREATE INDEX ix_patient_invitation_organization_status
    ON patient_invitation (organization_id, status, created_at DESC);
CREATE INDEX ix_patient_invitation_patient
    ON patient_invitation (patient_person_id, created_at DESC);
CREATE INDEX ix_patient_invitation_invited_by
    ON patient_invitation (invited_by_user_id);
CREATE INDEX ix_patient_invitation_accepted_by
    ON patient_invitation (accepted_by_user_id)
    WHERE accepted_by_user_id IS NOT NULL;

CREATE TABLE consent_record (
    id UUID PRIMARY KEY,
    organization_id UUID NOT NULL REFERENCES organization(id),
    patient_person_id UUID NOT NULL REFERENCES patient_person(id),
    user_id UUID NOT NULL REFERENCES app_user(id),
    invitation_id UUID NOT NULL UNIQUE REFERENCES patient_invitation(id),
    purpose VARCHAR(80) NOT NULL,
    text_version VARCHAR(60) NOT NULL,
    channel VARCHAR(30) NOT NULL CHECK (channel IN ('WEB_INVITATION')),
    accepted_at TIMESTAMPTZ NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX ix_consent_record_organization_patient
    ON consent_record (organization_id, patient_person_id, accepted_at DESC);
CREATE INDEX ix_consent_record_patient
    ON consent_record (patient_person_id);
CREATE INDEX ix_consent_record_user
    ON consent_record (user_id);

CREATE OR REPLACE FUNCTION prevent_immutable_record_mutation()
RETURNS TRIGGER AS $$
BEGIN
    RAISE EXCEPTION 'record is immutable';
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER consent_record_no_update
BEFORE UPDATE OR DELETE ON consent_record
FOR EACH ROW EXECUTE FUNCTION prevent_immutable_record_mutation();

ALTER TABLE patient_invitation ENABLE ROW LEVEL SECURITY;
ALTER TABLE consent_record ENABLE ROW LEVEL SECURITY;

ALTER FUNCTION prevent_immutable_record_mutation() SET search_path = pg_catalog;

REVOKE ALL ON patient_invitation FROM PUBLIC;
REVOKE ALL ON consent_record FROM PUBLIC;
REVOKE ALL ON FUNCTION prevent_immutable_record_mutation() FROM PUBLIC;

DO $$
DECLARE
    api_role TEXT;
BEGIN
    FOREACH api_role IN ARRAY ARRAY['anon', 'authenticated']
    LOOP
        IF EXISTS (SELECT 1 FROM pg_roles WHERE rolname = api_role) THEN
            EXECUTE format('REVOKE ALL ON patient_invitation FROM %I', api_role);
            EXECUTE format('REVOKE ALL ON consent_record FROM %I', api_role);
            EXECUTE format(
                'REVOKE ALL ON FUNCTION prevent_immutable_record_mutation() FROM %I',
                api_role
            );
        END IF;
    END LOOP;
END;
$$;
