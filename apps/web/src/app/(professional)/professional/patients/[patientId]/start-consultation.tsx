"use client";

import { ClipboardPlus, LoaderCircle } from "lucide-react";
import { useRouter } from "next/navigation";
import { useState, useTransition } from "react";
import { startConsultationAction } from "./actions";

export function StartConsultation({ patientId }: { patientId: string }) {
  const router = useRouter();
  const [message, setMessage] = useState("");
  const [pending, startTransition] = useTransition();

  function start() {
    startTransition(async () => {
      const result = await startConsultationAction(patientId);
      if (result.ok) router.refresh();
      else setMessage(result.message);
    });
  }

  return (
    <section className="consultation-empty" aria-labelledby="consultation-empty-title">
      <span><ClipboardPlus /></span>
      <div>
        <span className="eyebrow">Prontuário da consulta</span>
        <h2 id="consultation-empty-title">Comece um registro clínico</h2>
        <p>O rascunho será salvo automaticamente. A finalização cria uma versão imutável.</p>
      </div>
      <button className="button primary" type="button" onClick={start} disabled={pending}>
        {pending ? <LoaderCircle className="spin" size={17} /> : <ClipboardPlus size={17} />}
        {pending ? "Iniciando…" : "Iniciar consulta"}
      </button>
      {message ? <p className="form-error" role="alert">{message}</p> : null}
    </section>
  );
}
