"use server";

import { createApiClient, type EarlyAccessRequest } from "@nutrition-platform/api-client";

export type EarlyAccessState = {
  status: "idle" | "success" | "error";
  message: string;
};

export async function requestEarlyAccess(
  _previousState: EarlyAccessState,
  formData: FormData,
): Promise<EarlyAccessState> {
  const name = String(formData.get("name") ?? "").trim();
  const email = String(formData.get("email") ?? "").trim().toLowerCase();
  const currentTool = String(formData.get("currentTool") ?? "").trim();
  const marketingConsent = formData.get("marketingConsent") === "on";

  if (name.length < 2 || !email.includes("@") || currentTool.length < 2) {
    return { status: "error", message: "Revise seu nome, e-mail e ferramenta atual." };
  }

  try {
    const payload: EarlyAccessRequest = {
      name,
      email,
      currentTool,
      source: "landing-organic",
      marketingConsent,
      consentTextVersion: "early-access-v1",
    };
    const client = createApiClient(process.env.API_BASE_URL ?? "http://localhost:8080");
    const { error } = await client.POST("/api/v1/public/early-access", { body: payload });

    if (error) {
      return { status: "error", message: "Não foi possível registrar agora. Tente novamente." };
    }

    return {
      status: "success",
      message: "Interesse registrado. Entraremos em contato antes de qualquer convite ou cobrança.",
    };
  } catch {
    return {
      status: "error",
      message: "O serviço está temporariamente indisponível. Tente novamente em alguns instantes.",
    };
  }
}
