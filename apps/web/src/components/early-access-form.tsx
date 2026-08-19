"use client";

import { useActionState } from "react";
import { ArrowRight, CheckCircle2 } from "lucide-react";
import { requestEarlyAccess, type EarlyAccessState } from "@/app/(public)/actions";

const initialState: EarlyAccessState = { status: "idle", message: "" };

export function EarlyAccessForm() {
  const [state, action, pending] = useActionState(requestEarlyAccess, initialState);

  if (state.status === "success") {
    return (
      <div className="form-success" role="status">
        <CheckCircle2 aria-hidden="true" />
        <div>
          <strong>Você entrou na lista inicial.</strong>
          <p>{state.message}</p>
        </div>
      </div>
    );
  }

  return (
    <form action={action} className="access-form">
      <label>
        Seu nome
        <input name="name" autoComplete="name" minLength={2} required placeholder="Como podemos chamar você?" />
      </label>
      <label>
        E-mail profissional
        <input name="email" type="email" autoComplete="email" required placeholder="voce@consultorio.com.br" />
      </label>
      <label>
        Ferramenta atual
        <select name="currentTool" defaultValue="WebDiet" required>
          <option>WebDiet</option>
          <option>Dietbox</option>
          <option>Nutrium</option>
          <option>Planilha / documentos</option>
          <option>Outra</option>
        </select>
      </label>
      <label className="consent-field">
        <input name="marketingConsent" type="checkbox" />
        <span>Aceito receber novidades sobre o acesso antecipado. Esta permissão é opcional.</span>
      </label>
      {state.status === "error" ? <p className="form-error" role="alert">{state.message}</p> : null}
      <button className="button primary" type="submit" disabled={pending}>
        {pending ? "Registrando…" : "Quero participar"}
        {pending ? null : <ArrowRight size={17} aria-hidden="true" />}
      </button>
      <small>Usaremos estes dados somente para organizar o acesso inicial e a conversa de descoberta.</small>
    </form>
  );
}
