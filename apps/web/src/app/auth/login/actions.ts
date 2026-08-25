"use server";

import { headers } from "next/headers";
import { redirect } from "next/navigation";
import { createClient } from "@/lib/supabase/server";

function value(formData: FormData, key: string) {
  const entry = formData.get(key);
  return typeof entry === "string" ? entry.trim() : "";
}

function safeNext(formData: FormData) {
  const next = value(formData, "next");
  return next.startsWith("/") && !next.startsWith("//") ? next : "/professional";
}

async function callbackUrl(next: string) {
  const requestHeaders = await headers();
  const configuredOrigin = process.env.NEXT_PUBLIC_APP_URL;
  const origin = configuredOrigin ?? requestHeaders.get("origin") ?? "http://localhost:3000";
  const url = new URL("/auth/callback", origin);
  url.searchParams.set("next", next);
  return url.toString();
}

export async function signInWithPassword(formData: FormData) {
  const email = value(formData, "email");
  const password = value(formData, "password");
  const next = safeNext(formData);
  const supabase = await createClient();
  const { error } = await supabase.auth.signInWithPassword({ email, password });

  if (error) redirect(`/auth/login?status=invalid&next=${encodeURIComponent(next)}`);
  redirect(next);
}

export async function requestMagicLink(formData: FormData) {
  const email = value(formData, "email");
  const next = safeNext(formData);
  const supabase = await createClient();
  const { error } = await supabase.auth.signInWithOtp({
    email,
    options: {
      emailRedirectTo: await callbackUrl(next),
      shouldCreateUser: false,
    },
  });

  if (error) redirect(`/auth/login?status=unavailable&next=${encodeURIComponent(next)}`);
  redirect(`/auth/login?status=link-sent&next=${encodeURIComponent(next)}`);
}

export async function createProfessionalAccount(formData: FormData) {
  const email = value(formData, "email");
  const password = value(formData, "password");
  const next = safeNext(formData);
  const supabase = await createClient();
  const { data, error } = await supabase.auth.signUp({
    email,
    password,
    options: { emailRedirectTo: await callbackUrl(next) },
  });

  if (error) redirect(`/auth/login?status=unavailable&next=${encodeURIComponent(next)}`);
  if (data.session) redirect(next);
  redirect(`/auth/login?status=confirmation-sent&next=${encodeURIComponent(next)}`);
}

export async function signOut() {
  const supabase = await createClient();
  await supabase.auth.signOut();
  redirect("/auth/login?status=signed-out");
}
