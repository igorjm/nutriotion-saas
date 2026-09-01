"use client";

import type { PatientIntakeRecord } from "@nutrition-platform/api-client";
import { Save } from "lucide-react";
import { useActionState } from "react";
import { saveIntakeAction, type FormState } from "./actions";

const initialState: FormState = { status: "idle", message: "" };

type IntakeFormProps = {
  patientId: string;
  intake: PatientIntakeRecord | null;
};

export function IntakeForm({ patientId, intake }: IntakeFormProps) {
  const saveAction = saveIntakeAction.bind(null, patientId);
  const [state, action, pending] = useActionState(saveAction, initialState);

  return (
    <section className="clinical-card" aria-labelledby="intake-title">
      <div className="clinical-card-heading">
        <div>
          <span className="eyebrow">Anamnese estruturada</span>
          <h2 id="intake-title">Contexto do cuidado</h2>
          <p>Registre o que é necessário para orientar a consulta. Campos vazios são permitidos.</p>
        </div>
        {intake ? <span className="version-badge">Versão {intake.version}</span> : null}
      </div>

      <form action={action} className="intake-form">
        <label>
          Objetivo do acompanhamento
          <textarea name="careGoal" defaultValue={intake?.careGoal ?? ""} maxLength={2000} rows={3} />
        </label>
        <div className="clinical-form-grid">
          <label>
            Alergias
            <textarea name="allergies" defaultValue={intake?.allergies ?? ""} maxLength={2000} rows={3} />
          </label>
          <label>
            Restrições alimentares
            <textarea
              name="foodRestrictions"
              defaultValue={intake?.foodRestrictions ?? ""}
              maxLength={2000}
              rows={3}
            />
          </label>
        </div>
        <label>
          Histórico clínico relevante
          <textarea
            name="clinicalHistory"
            defaultValue={intake?.clinicalHistory ?? ""}
            maxLength={5000}
            rows={4}
          />
        </label>
        <label>
          Rotina e contexto alimentar
          <textarea name="routineNotes" defaultValue={intake?.routineNotes ?? ""} maxLength={5000} rows={4} />
        </label>
        <div className="form-action-row">
          <button className="button secondary" type="submit" disabled={pending}>
            <Save size={16} /> {pending ? "Salvando…" : "Salvar anamnese"}
          </button>
          {state.status !== "idle" ? (
            <p className={state.status === "error" ? "inline-status error" : "inline-status"} role={state.status === "error" ? "alert" : "status"}>
              {state.message}
            </p>
          ) : null}
        </div>
      </form>
    </section>
  );
}
