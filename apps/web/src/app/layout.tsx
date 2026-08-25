import type { Metadata } from "next";
import "./globals.css";

export const metadata: Metadata = {
  title: {
    default: "[PRODUCT_NAME] — Nutrição com continuidade",
    template: "%s · [PRODUCT_NAME]",
  },
  description:
    "Uma plataforma brasileira para nutricionistas cuidarem da prática, dos pacientes e do próprio crescimento.",
};

export default function RootLayout({ children }: Readonly<{ children: React.ReactNode }>) {
  return (
    <html lang="pt-BR" data-scroll-behavior="smooth">
      <body>{children}</body>
    </html>
  );
}
