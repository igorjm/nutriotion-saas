import createClient from "openapi-fetch";
import type { components, paths } from "./schema";

export type SessionContext = components["schemas"]["SessionContext"];
export type PatientSummary = components["schemas"]["PatientSummary"];
export type PatientListItem = components["schemas"]["PatientListItem"];
export type CreatePatientInvitationRequest = components["schemas"]["CreatePatientInvitationRequest"];
export type PatientInvitationCreated = components["schemas"]["PatientInvitationCreated"];
export type PatientInvitationPreview = components["schemas"]["PatientInvitationPreview"];
export type EarlyAccessRequest = components["schemas"]["EarlyAccessRequest"];

export function createApiClient(baseUrl: string, accessToken?: string) {
  return createClient<paths>({
    baseUrl,
    headers: accessToken ? { Authorization: `Bearer ${accessToken}` } : undefined,
  });
}
