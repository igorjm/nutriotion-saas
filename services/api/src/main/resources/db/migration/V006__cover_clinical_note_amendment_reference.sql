DROP INDEX ix_clinical_note_amended_version;

CREATE INDEX ix_clinical_note_amended_version
    ON clinical_note_version (organization_id, amends_note_version_id)
    WHERE amends_note_version_id IS NOT NULL;
