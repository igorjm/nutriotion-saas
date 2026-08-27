"use server";

import { headers } from "next/headers";
import { redirect } from "next/navigation";
import { acceptPatientInvitation } from "@/lib/api/server";
import { createClient } from "@/lib/supabase/server";

function value(formData: FormData, key: string) {
  const entry = formData.get(key);
  return typeof entry === "string" ? entry.trim() : "";
}

function safeToken(formData: FormData) {
  const token = value(formData, "token");
  return /^[A-Za-z0-9_-]{32,128}$/.test(token) ? token : "";
}

async function callbackUrl(next: string) {
  const requestHeaders = await headers();
  const origin = process.env.NEXT_PUBLIC_APP_URL ?? requestHeaders.get("origin") ?? "http://localhost:3000";
  const url = new URL("/auth/callback", origin);
  url.searchParams.set("next", next);
  return url.toString();
}

export async function createInvitedPatientAccount(formData: FormData) {
  const token = safeToken(formData);
  const email = value(formData, "email").toLowerCase();
  const password = value(formData, "password");
  if (!token) redirect("/auth/login?status=callback-error");
  const destination = `/convite/${token}`;

  const supabase = await createClient();
  const { data, error } = await supabase.auth.signUp({
    email,
    password,
    options: { emailRedirectTo: await callbackUrl(destination) },
  });

  if (error) redirect(`${destination}?status=signup-error`);
  if (data.session) redirect(`${destination}?status=account-ready`);
  redirect(`${destination}?status=confirmation-sent`);
}

export async function acceptInvitation(formData: FormData) {
  const token = safeToken(formData);
  const accepted = value(formData, "consent") === "accepted";
  const consentTextVersion = value(formData, "consentTextVersion");
  if (!token || !accepted || consentTextVersion !== "care-relationship-v1") {
    redirect(token ? `/convite/${token}?status=consent-required` : "/auth/login?status=callback-error");
  }

  const completed = await acceptPatientInvitation(token, consentTextVersion);
  if (!completed) redirect(`/convite/${token}?status=accept-error`);
  redirect("/patient/welcome?status=accepted");
}

export async function signOutFromInvitation(formData: FormData) {
  const token = safeToken(formData);
  const supabase = await createClient();
  await supabase.auth.signOut();
  redirect(token ? `/convite/${token}` : "/");
}
