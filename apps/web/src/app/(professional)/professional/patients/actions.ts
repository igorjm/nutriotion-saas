"use server";

import { createPatientInvitation } from "@/lib/api/server";

export type InvitationFormState = {
  status: "idle" | "success" | "error";
  message: string;
  invitationPath?: string;
};

function value(formData: FormData, key: string) {
  const entry = formData.get(key);
  return typeof entry === "string" ? entry.trim() : "";
}

export async function invitePatient(
  _previousState: InvitationFormState,
  formData: FormData,
): Promise<InvitationFormState> {
  const displayName = value(formData, "displayName");
  const email = value(formData, "email").toLowerCase();
  const careFocus = value(formData, "careFocus");

  if (displayName.length < 2 || !email.includes("@")) {
    return { status: "error", message: "Revise o nome e o e-mail do paciente." };
  }

  const invitation = await createPatientInvitation({
    displayName,
    email,
    careFocus: careFocus || null,
  });
  if (!invitation) {
    return {
      status: "error",
      message: "Não foi possível criar o convite. Confira se já existe um convite pendente para esse e-mail.",
    };
  }

  return {
    status: "success",
    message: "Convite criado. Use o link abaixo para testar a experiência do paciente.",
    invitationPath: `/convite/${invitation.token}`,
  };
}
