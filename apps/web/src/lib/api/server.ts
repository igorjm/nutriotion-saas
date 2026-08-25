import "server-only";

import { createApiClient, type SessionContext } from "@nutrition-platform/api-client";
import { createClient as createSupabaseClient } from "@/lib/supabase/server";

export async function getSessionContext(): Promise<SessionContext | null> {
  const supabase = await createSupabaseClient();
  const { data: claimsData, error: claimsError } = await supabase.auth.getClaims();
  if (claimsError || !claimsData?.claims) return null;

  const { data: sessionData } = await supabase.auth.getSession();
  const token = sessionData.session?.access_token;
  if (!token) return null;

  const client = createApiClient(process.env.API_BASE_URL ?? "http://localhost:8080", token);
  try {
    const { data, error } = await client.GET("/api/v1/me/context");

    if (error || !data) return null;
    return data;
  } catch {
    return null;
  }
}
