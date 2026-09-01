import "server-only";

import {
  createApiClient,
  type ConsultationWorkspace,
  type CreateAmendmentRequest,
  type CreatePatientInvitationRequest,
  type PatientClinicalRecord,
  type PatientInvitationCreated,
  type PatientInvitationPreview,
  type PatientListItem,
  type SessionContext,
  type UpdateClinicalNoteRequest,
  type UpdatePatientIntakeRequest,
} from "@nutrition-platform/api-client";
import { createClient as createSupabaseClient } from "@/lib/supabase/server";

async function getAuthenticatedClient() {
  const supabase = await createSupabaseClient();
  const { data: claimsData, error: claimsError } = await supabase.auth.getClaims();
  if (claimsError || !claimsData?.claims) return null;

  const { data: sessionData } = await supabase.auth.getSession();
  const token = sessionData.session?.access_token;
  if (!token) return null;

  return createApiClient(process.env.API_BASE_URL ?? "http://localhost:8080", token);
}

export async function getSessionContext(): Promise<SessionContext | null> {
  const client = await getAuthenticatedClient();
  if (!client) return null;

  try {
    const { data, error } = await client.GET("/api/v1/me/context");

    if (error || !data) return null;
    return data;
  } catch {
    return null;
  }
}

export async function getPatients(): Promise<PatientListItem[] | null> {
  const client = await getAuthenticatedClient();
  if (!client) return null;
  try {
    const { data, error } = await client.GET("/api/v1/patients");
    return error || !data ? null : data;
  } catch {
    return null;
  }
}

export async function getPatientClinicalRecord(
  patientId: string,
): Promise<PatientClinicalRecord | null> {
  const client = await getAuthenticatedClient();
  if (!client) return null;
  try {
    const { data, error } = await client.GET("/api/v1/patients/{patientId}/clinical-record", {
      params: { path: { patientId } },
    });
    return error || !data ? null : data;
  } catch {
    return null;
  }
}

export async function updatePatientIntake(
  patientId: string,
  payload: UpdatePatientIntakeRequest,
) {
  const client = await getAuthenticatedClient();
  if (!client) return null;
  try {
    const { data, error } = await client.PUT("/api/v1/patients/{patientId}/intake", {
      params: { path: { patientId } },
      body: payload,
    });
    return error || !data ? null : data;
  } catch {
    return null;
  }
}

export async function startPatientConsultation(
  patientId: string,
): Promise<ConsultationWorkspace | null> {
  const client = await getAuthenticatedClient();
  if (!client) return null;
  try {
    const { data, error } = await client.POST("/api/v1/patients/{patientId}/consultations", {
      params: { path: { patientId } },
    });
    return error || !data ? null : data;
  } catch {
    return null;
  }
}

export async function saveClinicalNoteDraft(
  patientId: string,
  consultationId: string,
  payload: UpdateClinicalNoteRequest,
): Promise<ConsultationWorkspace | null> {
  const client = await getAuthenticatedClient();
  if (!client) return null;
  try {
    const { data, error } = await client.PUT(
      "/api/v1/patients/{patientId}/consultations/{consultationId}/note",
      {
        params: { path: { patientId, consultationId } },
        body: payload,
      },
    );
    return error || !data ? null : data;
  } catch {
    return null;
  }
}

export async function finalizeClinicalNote(
  patientId: string,
  consultationId: string,
): Promise<ConsultationWorkspace | null> {
  const client = await getAuthenticatedClient();
  if (!client) return null;
  try {
    const { data, error } = await client.POST(
      "/api/v1/patients/{patientId}/consultations/{consultationId}/finalize",
      { params: { path: { patientId, consultationId } } },
    );
    return error || !data ? null : data;
  } catch {
    return null;
  }
}

export async function startClinicalNoteAmendment(
  patientId: string,
  consultationId: string,
  payload: CreateAmendmentRequest,
): Promise<ConsultationWorkspace | null> {
  const client = await getAuthenticatedClient();
  if (!client) return null;
  try {
    const { data, error } = await client.POST(
      "/api/v1/patients/{patientId}/consultations/{consultationId}/amendments",
      {
        params: { path: { patientId, consultationId } },
        body: payload,
      },
    );
    return error || !data ? null : data;
  } catch {
    return null;
  }
}

export async function createPatientInvitation(
  payload: CreatePatientInvitationRequest,
): Promise<PatientInvitationCreated | null> {
  const client = await getAuthenticatedClient();
  if (!client) return null;
  try {
    const { data, error } = await client.POST("/api/v1/patient-invitations", { body: payload });
    return error || !data ? null : data;
  } catch {
    return null;
  }
}

export async function getPatientInvitationPreview(
  token: string,
): Promise<PatientInvitationPreview | null> {
  const client = createApiClient(process.env.API_BASE_URL ?? "http://localhost:8080");
  try {
    const { data, error } = await client.GET("/api/v1/public/patient-invitations/{token}", {
      params: { path: { token } },
    });
    return error || !data ? null : data;
  } catch {
    return null;
  }
}

export async function acceptPatientInvitation(
  token: string,
  consentTextVersion: "care-relationship-v1",
) {
  const client = await getAuthenticatedClient();
  if (!client) return false;
  try {
    const { data, error } = await client.POST("/api/v1/patient-invitations/{token}/accept", {
      params: { path: { token } },
      body: { consentTextVersion },
    });
    return !error && Boolean(data);
  } catch {
    return false;
  }
}
