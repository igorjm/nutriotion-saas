"use client";

import type { ConsultationWorkspace, UpdateClinicalNoteRequest } from "@nutrition-platform/api-client";
import { CheckCircle2, FileLock2, History, LoaderCircle, Save, ShieldCheck } from "lucide-react";
import { useCallback, useEffect, useRef, useState, useTransition } from "react";
import {
  finalizeClinicalNoteAction,
  saveClinicalNoteAction,
  startAmendmentAction,
  type ClinicalMutationResult,
} from "./actions";

type ConsultationEditorProps = {
  patientId: string;
  initialWorkspace: ConsultationWorkspace;
};

function notePayload(workspace: ConsultationWorkspace): UpdateClinicalNoteRequest {
  return {
    subjective: workspace.note.subjective,
    objective: workspace.note.objective,
    assessment: workspace.note.assessment,
    agreedActions: workspace.note.agreedActions,
  };
}

function formatDate(value: string) {
  return new Intl.DateTimeFormat("pt-BR", {
    dateStyle: "short",
    timeStyle: "short",
  }).format(new Date(value));
}

export function ConsultationEditor({ patientId, initialWorkspace }: ConsultationEditorProps) {
  const [workspace, setWorkspace] = useState(initialWorkspace);
  const [draft, setDraft] = useState(() => notePayload(initialWorkspace));
  const [saveStatus, setSaveStatus] = useState<"idle" | "waiting" | "saving" | "saved" | "error">("idle");
  const [savedAt, setSavedAt] = useState(initialWorkspace.note.updatedAt);
  const [message, setMessage] = useState("");
  const [amendmentReason, setAmendmentReason] = useState("");
  const [busy, startTransition] = useTransition();
  const firstRender = useRef(true);
  const timer = useRef<ReturnType<typeof setTimeout> | null>(null);
  const saveChain = useRef<Promise<void>>(Promise.resolve());

  const enqueueSave = useCallback((payload: UpdateClinicalNoteRequest) => {
    setSaveStatus("saving");
    const request = saveChain.current.then(() =>
      saveClinicalNoteAction(patientId, workspace.id, payload),
    );
    saveChain.current = request.then(() => undefined, () => undefined);
    void request.then((result) => {
      if (result.ok && result.workspace) {
        setSavedAt(result.workspace.note.updatedAt);
        setSaveStatus("saved");
        setMessage("");
      } else {
        setSaveStatus("error");
        setMessage(result.message);
      }
    });
    return request;
  }, [patientId, workspace.id]);

  useEffect(() => {
    if (workspace.status !== "IN_PROGRESS") return;
    if (firstRender.current) {
      firstRender.current = false;
      return;
    }
    if (timer.current) clearTimeout(timer.current);
    setSaveStatus("waiting");
    timer.current = setTimeout(() => {
      void enqueueSave(draft);
    }, 800);
    return () => {
      if (timer.current) clearTimeout(timer.current);
    };
  }, [draft, enqueueSave, workspace.status]);

  function updateDraft(field: keyof UpdateClinicalNoteRequest, value: string) {
    setDraft((current) => ({ ...current, [field]: value }));
  }

  function finalize() {
    if (!Object.values(draft).some((value) => value.trim().length > 0)) {
      setMessage("Preencha ao menos um campo clínico antes de finalizar.");
      setSaveStatus("error");
      return;
    }
    if (!window.confirm("Finalizar este registro? Depois disso, qualquer correção exigirá uma retificação versionada.")) return;
    if (timer.current) clearTimeout(timer.current);
    startTransition(async () => {
      const saved = await enqueueSave(draft);
      if (!saved.ok) return;
      const result = await finalizeClinicalNoteAction(patientId, workspace.id);
      applyWorkspaceResult(result);
    });
  }

  function amend() {
    startTransition(async () => {
      const result = await startAmendmentAction(patientId, workspace.id, amendmentReason);
      if (result.ok && result.workspace) {
        setWorkspace(result.workspace);
        setDraft(notePayload(result.workspace));
        setAmendmentReason("");
        setSaveStatus("saved");
        setSavedAt(result.workspace.note.updatedAt);
      }
      setMessage(result.message);
    });
  }

  function applyWorkspaceResult(result: ClinicalMutationResult) {
    if (result.ok && result.workspace) {
      setWorkspace(result.workspace);
      setDraft(notePayload(result.workspace));
      setSavedAt(result.workspace.note.updatedAt);
      setSaveStatus("saved");
    } else {
      setSaveStatus("error");
    }
    setMessage(result.message);
  }

  const finalized = workspace.status === "FINALIZED";
  const statusCopy = saveStatus === "waiting"
    ? "Alterações pendentes"
    : saveStatus === "saving"
      ? "Salvando…"
      : saveStatus === "saved"
        ? `Salvo em ${formatDate(savedAt)}`
        : saveStatus === "error"
          ? "Falha ao salvar"
          : "Salvamento automático ativo";

  return (
    <section className="clinical-card consultation-card" aria-labelledby="consultation-title">
      <div className="clinical-card-heading consultation-heading">
        <div>
          <span className="eyebrow">Prontuário da consulta</span>
          <h2 id="consultation-title">Registro clínico</h2>
          <p>Consulta iniciada em {formatDate(workspace.createdAt)}</p>
        </div>
        <div className="clinical-version-status">
          <span className="version-badge">Versão {workspace.note.version}</span>
          <span className={`note-status ${finalized ? "finalized" : "draft"}`}>
            {finalized ? <FileLock2 size={14} /> : <Save size={14} />}
            {finalized ? "Finalizado" : "Rascunho"}
          </span>
        </div>
      </div>

      {workspace.note.amendmentReason ? (
        <div className="amendment-context">
          <History size={18} />
          <p><strong>Motivo da retificação</strong>{workspace.note.amendmentReason}</p>
        </div>
      ) : null}

      <div className="clinical-note-form">
        <label>
          Subjetivo · relato e percepção do paciente
          <textarea
            value={draft.subjective}
            onChange={(event) => updateDraft("subjective", event.target.value)}
            maxLength={10000}
            rows={5}
            readOnly={finalized}
          />
        </label>
        <label>
          Objetivo · observações e dados relevantes
          <textarea
            value={draft.objective}
            onChange={(event) => updateDraft("objective", event.target.value)}
            maxLength={10000}
            rows={5}
            readOnly={finalized}
          />
        </label>
        <label>
          Avaliação profissional
          <textarea
            value={draft.assessment}
            onChange={(event) => updateDraft("assessment", event.target.value)}
            maxLength={10000}
            rows={5}
            readOnly={finalized}
          />
        </label>
        <label>
          Ações combinadas com o paciente
          <textarea
            value={draft.agreedActions}
            onChange={(event) => updateDraft("agreedActions", event.target.value)}
            maxLength={10000}
            rows={5}
            readOnly={finalized}
          />
        </label>
      </div>

      {!finalized ? (
        <div className="consultation-action-row">
          <p className={`autosave-status ${saveStatus === "error" ? "error" : ""}`} role={saveStatus === "error" ? "alert" : "status"}>
            {saveStatus === "saving" ? <LoaderCircle className="spin" /> : saveStatus === "saved" ? <CheckCircle2 /> : <Save />}
            {statusCopy}
          </p>
          <button className="button primary" type="button" onClick={finalize} disabled={busy}>
            <ShieldCheck size={17} /> {busy ? "Finalizando…" : "Finalizar registro"}
          </button>
        </div>
      ) : (
        <div className="finalized-panel">
          <div>
            <FileLock2 />
            <p><strong>Registro imutável</strong>Finalizado em {formatDate(workspace.note.finalizedAt ?? workspace.note.updatedAt)}. O conteúdo fica preservado para auditoria.</p>
          </div>
          <label>
            Motivo da retificação
            <input
              value={amendmentReason}
              onChange={(event) => setAmendmentReason(event.target.value)}
              minLength={5}
              maxLength={500}
              placeholder="Ex.: correção após revisão com o paciente"
            />
          </label>
          <button className="button secondary" type="button" onClick={amend} disabled={busy}>
            <History size={16} /> {busy ? "Abrindo…" : "Criar retificação"}
          </button>
        </div>
      )}

      {message ? <p className={saveStatus === "error" ? "form-error consultation-message" : "inline-status consultation-message"} role={saveStatus === "error" ? "alert" : "status"}>{message}</p> : null}
    </section>
  );
}
