import { brand } from "@nutrition-platform/brand";
import type { Metadata } from "next";
import "./globals.css";

export const metadata: Metadata = {
  metadataBase: new URL(
    process.env.NEXT_PUBLIC_SITE_URL ?? "https://nutrition-practice-prototype.igorjmelo4.chatgpt.site",
  ),
  applicationName: brand.name,
  title: `${brand.name} — ${brand.promise} · Protótipo`,
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
  other: { "codex-preview": "development" },
};

export const viewport = {
  themeColor: brand.themeColor,
  colorScheme: "light",
};

export default function RootLayout({ children }: Readonly<{ children: React.ReactNode }>) {
  return <html lang="pt-BR"><body>{children}</body></html>;
}
