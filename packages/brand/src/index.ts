export const brand = {
  name: "Vincelia",
  shortName: "Vincelia",
  descriptor: "Plataforma de cuidado nutricional",
  promise: "Cuidado que continua.",
  commercialHeadline: "Mais tempo para cuidar. Mais clareza para crescer.",
  description:
    "Uma plataforma brasileira para nutricionistas cuidarem da prática, dos pacientes e do próprio crescimento.",
  locale: "pt-BR",
  themeColor: "#173f3b",
  backgroundColor: "#f7f5ef",
  candidateDomains: {
    marketing: "vincelia.com.br",
    application: "app.vincelia.com.br",
    api: "api.vincelia.com.br",
  },
} as const;

export type Brand = typeof brand;
