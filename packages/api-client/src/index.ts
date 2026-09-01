import createClient from "openapi-fetch";
import type { components, paths } from "./schema";

export type SessionContext = components["schemas"]["SessionContext"];
export type PatientSummary = components["schemas"]["PatientSummary"];
export type PatientListItem = components["schemas"]["PatientListItem"];
export type CreatePatientInvitationRequest = components["schemas"]["CreatePatientInvitationRequest"];
export type PatientInvitationCreated = components["schemas"]["PatientInvitationCreated"];
export type PatientInvitationPreview = components["schemas"]["PatientInvitationPreview"];
export type EarlyAccessRequest = components["schemas"]["EarlyAccessRequest"];
export type PatientClinicalRecord = components["schemas"]["PatientClinicalRecord"];
export type PatientIntakeRecord = components["schemas"]["PatientIntakeRecord"];
export type UpdatePatientIntakeRequest = components["schemas"]["UpdatePatientIntakeRequest"];
export type ConsultationWorkspace = components["schemas"]["ConsultationWorkspace"];
export type ClinicalNoteRecord = components["schemas"]["ClinicalNoteRecord"];
export type UpdateClinicalNoteRequest = components["schemas"]["UpdateClinicalNoteRequest"];
export type CreateAmendmentRequest = components["schemas"]["CreateAmendmentRequest"];

export function createApiClient(baseUrl: string, accessToken?: string) {
  return createClient<paths>({
    baseUrl,
    headers: accessToken ? { Authorization: `Bearer ${accessToken}` } : undefined,
  });
}
