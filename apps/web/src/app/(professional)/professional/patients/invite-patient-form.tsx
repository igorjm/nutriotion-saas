"use client";

import { useActionState } from "react";
import { CheckCircle2, ExternalLink, ShieldCheck, UserPlus } from "lucide-react";
import { invitePatient, type InvitationFormState } from "./actions";

const initialInvitationState: InvitationFormState = {
  status: "idle",
  message: "",
};

export function InvitePatientForm() {
  const [state, action, pending] = useActionState(invitePatient, initialInvitationState);

  return (
    <section className="invite-patient-card" aria-labelledby="invite-title">
      <div className="section-card-heading">
        <span className="foundation-icon"><UserPlus /></span>
        <div>
          <span className="eyebrow">Novo vínculo</span>
          <h2 id="invite-title">Convidar paciente fictício</h2>
          <p>Cadastre apenas dados inventados neste ambiente de desenvolvimento.</p>
        </div>
      </div>

      <form action={action} className="invite-patient-form">
        <label>
          Nome completo
          <input name="displayName" placeholder="Ex.: Ana Souza" minLength={2} maxLength={160} required />
        </label>
        <label>
          E-mail fictício
          <input name="email" type="email" placeholder="ana.teste@example.invalid" maxLength={254} required />
        </label>
        <label>
          Foco inicial
          <select name="careFocus" defaultValue="Educação alimentar">
            <option>Educação alimentar</option>
            <option>Performance e rotina</option>
            <option>Saúde clínica</option>
            <option>Organização da alimentação</option>
          </select>
        </label>
        <div className="consent-info compact">
          <ShieldCheck />
          <p>O vínculo só fica ativo quando o paciente cria a própria conta e aceita o consentimento versionado.</p>
        </div>
        <button className="button primary" type="submit" disabled={pending}>
          <UserPlus size={17} /> {pending ? "Criando convite…" : "Criar convite de teste"}
        </button>
      </form>

      {state.status === "error" ? <p className="form-error" role="alert">{state.message}</p> : null}
      {state.status === "success" && state.invitationPath ? (
        <div className="invitation-success" role="status">
          <CheckCircle2 />
          <div>
            <strong>{state.message}</strong>
            <p>Abra em uma janela anônima para criar uma conta diferente da conta profissional.</p>
            <a className="invitation-link" href={state.invitationPath} target="_blank" rel="noreferrer">
              <ExternalLink size={15} /> Abrir convite do paciente
            </a>
          </div>
        </div>
      ) : null}
    </section>
  );
}
