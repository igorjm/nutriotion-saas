import "server-only";

import { cookies } from "next/headers";
import { createApiClient, type SessionContext } from "@nutrition-platform/api-client";

export async function getSessionContext(): Promise<SessionContext | null> {
  const token = (await cookies()).get("access_token")?.value;
  if (!token) return null;

  const client = createApiClient(process.env.API_BASE_URL ?? "http://localhost:8080", token);
  const { data, error } = await client.GET("/api/v1/me/context");

  if (error || !data) return null;
  return data;
}
