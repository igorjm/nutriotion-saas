CREATE TABLE app_user (
    id UUID PRIMARY KEY,
    external_subject VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(254) NOT NULL,
    display_name VARCHAR(120) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE UNIQUE INDEX ux_app_user_email_lower ON app_user ((lower(email)));

CREATE TABLE organization (
    id UUID PRIMARY KEY,
    name VARCHAR(160) NOT NULL,
    slug VARCHAR(100) NOT NULL UNIQUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE membership (
    id UUID PRIMARY KEY,
    organization_id UUID NOT NULL REFERENCES organization(id),
    user_id UUID NOT NULL REFERENCES app_user(id),
    role VARCHAR(30) NOT NULL CHECK (role IN ('OWNER', 'NUTRITIONIST')),
    status VARCHAR(20) NOT NULL CHECK (status IN ('ACTIVE', 'REVOKED')),
    is_default BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (organization_id, user_id)
);

CREATE UNIQUE INDEX ux_membership_default_user
    ON membership (user_id)
    WHERE is_default = TRUE AND status = 'ACTIVE';
CREATE INDEX ix_membership_organization ON membership (organization_id, status);

CREATE TABLE patient_person (
    id UUID PRIMARY KEY,
    display_name VARCHAR(160) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE care_relationship (
    id UUID PRIMARY KEY,
    organization_id UUID NOT NULL REFERENCES organization(id),
    patient_person_id UUID NOT NULL REFERENCES patient_person(id),
    status VARCHAR(20) NOT NULL CHECK (status IN ('INVITED', 'ACTIVE', 'PAUSED', 'ARCHIVED')),
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (organization_id, patient_person_id)
);

CREATE INDEX ix_care_relationship_organization
    ON care_relationship (organization_id, status, patient_person_id);

CREATE TABLE early_access_lead (
    id UUID PRIMARY KEY,
    name VARCHAR(120) NOT NULL,
    email VARCHAR(254) NOT NULL,
    current_tool VARCHAR(80) NOT NULL,
    source VARCHAR(120) NOT NULL,
    marketing_consent BOOLEAN NOT NULL,
    consent_text_version VARCHAR(60) NOT NULL,
    received_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE UNIQUE INDEX ux_early_access_lead_email_lower ON early_access_lead ((lower(email)));

CREATE TABLE audit_event (
    id UUID PRIMARY KEY,
    organization_id UUID NULL REFERENCES organization(id),
    actor_subject VARCHAR(255) NULL,
    action VARCHAR(100) NOT NULL,
    aggregate_type VARCHAR(80) NOT NULL,
    aggregate_id UUID NULL,
    metadata JSONB NOT NULL DEFAULT '{}'::jsonb,
    occurred_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX ix_audit_event_organization_time ON audit_event (organization_id, occurred_at DESC);

CREATE OR REPLACE FUNCTION prevent_audit_mutation()
RETURNS TRIGGER AS $$
BEGIN
    RAISE EXCEPTION 'audit_event is append-only';
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER audit_event_no_update
BEFORE UPDATE OR DELETE ON audit_event
FOR EACH ROW EXECUTE FUNCTION prevent_audit_mutation();

CREATE TABLE outbox_event (
    id UUID PRIMARY KEY,
    organization_id UUID NULL REFERENCES organization(id),
    event_type VARCHAR(120) NOT NULL,
    aggregate_type VARCHAR(80) NOT NULL,
    aggregate_id UUID NULL,
    payload JSONB NOT NULL,
    occurred_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    processed_at TIMESTAMPTZ NULL,
    attempts INTEGER NOT NULL DEFAULT 0 CHECK (attempts >= 0)
);

CREATE INDEX ix_outbox_event_pending ON outbox_event (occurred_at) WHERE processed_at IS NULL;
