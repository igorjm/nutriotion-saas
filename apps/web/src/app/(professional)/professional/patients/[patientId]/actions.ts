"use server";

import type {
  ConsultationWorkspace,
  UpdateClinicalNoteRequest,
} from "@nutrition-platform/api-client";
import { revalidatePath } from "next/cache";
import {
  finalizeClinicalNote,
  saveClinicalNoteDraft,
  startClinicalNoteAmendment,
  startPatientConsultation,
  updatePatientIntake,
} from "@/lib/api/server";

export type FormState = {
  status: "idle" | "success" | "error";
  message: string;
};

export type ClinicalMutationResult = {
  ok: boolean;
  message: string;
  workspace?: ConsultationWorkspace;
};

function value(formData: FormData, key: string) {
  const entry = formData.get(key);
  return typeof entry === "string" ? entry.trim() : "";
}

export async function saveIntakeAction(
  patientId: string,
  _previousState: FormState,
  formData: FormData,
): Promise<FormState> {
  const intake = await updatePatientIntake(patientId, {
    allergies: value(formData, "allergies"),
    foodRestrictions: value(formData, "foodRestrictions"),
    clinicalHistory: value(formData, "clinicalHistory"),
    routineNotes: value(formData, "routineNotes"),
    careGoal: value(formData, "careGoal"),
  });
  if (!intake) {
    return { status: "error", message: "Não foi possível salvar a anamnese. Tente novamente." };
  }

  revalidatePath(`/professional/patients/${patientId}`);
  return { status: "success", message: `Anamnese salva · versão ${intake.version}` };
}

export async function startConsultationAction(patientId: string): Promise<ClinicalMutationResult> {
  const workspace = await startPatientConsultation(patientId);
  if (!workspace) {
    return { ok: false, message: "Não foi possível iniciar a consulta." };
  }
  revalidatePath(`/professional/patients/${patientId}`);
  return { ok: true, message: "Consulta iniciada.", workspace };
}

export async function saveClinicalNoteAction(
  patientId: string,
  consultationId: string,
  payload: UpdateClinicalNoteRequest,
): Promise<ClinicalMutationResult> {
  const workspace = await saveClinicalNoteDraft(patientId, consultationId, payload);
  if (!workspace) {
    return {
      ok: false,
      message: "O rascunho não foi salvo. Mantenha esta página aberta e tente novamente.",
    };
  }
  return { ok: true, message: "Rascunho salvo.", workspace };
}

export async function finalizeClinicalNoteAction(
  patientId: string,
  consultationId: string,
): Promise<ClinicalMutationResult> {
  const workspace = await finalizeClinicalNote(patientId, consultationId);
  if (!workspace) {
    return {
      ok: false,
      message: "Não foi possível finalizar. Preencha ao menos um campo clínico e tente novamente.",
    };
  }
  revalidatePath(`/professional/patients/${patientId}`);
  return { ok: true, message: "Registro finalizado e protegido contra alterações.", workspace };
}

export async function startAmendmentAction(
  patientId: string,
  consultationId: string,
  reason: string,
): Promise<ClinicalMutationResult> {
  const normalizedReason = reason.trim();
  if (normalizedReason.length < 5) {
    return { ok: false, message: "Explique o motivo da retificação em pelo menos 5 caracteres." };
  }
  const workspace = await startClinicalNoteAmendment(patientId, consultationId, {
    reason: normalizedReason,
  });
  if (!workspace) {
    return { ok: false, message: "Não foi possível abrir a retificação." };
  }
  revalidatePath(`/professional/patients/${patientId}`);
  return { ok: true, message: "Retificação aberta. O registro anterior continua preservado.", workspace };
}
