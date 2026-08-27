ALTER TABLE care_relationship
    ADD COLUMN care_focus VARCHAR(120) NULL;

UPDATE care_relationship cr
SET care_focus = p.care_focus
FROM patient_person p
WHERE p.id = cr.patient_person_id;

ALTER TABLE patient_person
    DROP COLUMN care_focus;

ALTER TABLE consent_record
    ADD COLUMN text_snapshot TEXT NOT NULL DEFAULT (
        'Ao aceitar, você permite que esta organização crie e mantenha seu registro de acompanhamento nutricional, acesse as informações que você decidir fornecer e entre em contato sobre este cuidado.'
        || E'\n\n'
        || 'Este consentimento não inclui marketing, não autoriza decisões clínicas automáticas e poderá ser revisto ou retirado pelos canais de privacidade quando esse fluxo estiver disponível.'
    );

ALTER TABLE consent_record
    ALTER COLUMN text_snapshot DROP DEFAULT;
