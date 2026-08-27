import { brand } from "@nutrition-platform/brand";
import type { Metadata } from "next";
import "./globals.css";

const appUrl = process.env.NEXT_PUBLIC_APP_URL ?? "http://localhost:3000";

export const metadata: Metadata = {
  metadataBase: new URL(appUrl),
  applicationName: brand.name,
  title: {
    default: `${brand.name} — ${brand.promise}`,
    template: `%s · ${brand.name}`,
  },
  description: brand.description,
  manifest: "/manifest.webmanifest",
  openGraph: {
    type: "website",
    locale: "pt_BR",
    siteName: brand.name,
    title: `${brand.name} — ${brand.promise}`,
    description: brand.description,
    images: [{ url: "/og.png", width: 1200, height: 630, alt: `${brand.name} — ${brand.promise}` }],
  },
  twitter: {
    card: "summary_large_image",
    title: `${brand.name} — ${brand.promise}`,
    description: brand.description,
    images: ["/og.png"],
  },
  category: "health",
};

export const viewport = {
  themeColor: brand.themeColor,
  colorScheme: "light",
};

export default function RootLayout({ children }: Readonly<{ children: React.ReactNode }>) {
  return (
    <html lang="pt-BR" data-scroll-behavior="smooth">
      <body>{children}</body>
    </html>
  );
}
