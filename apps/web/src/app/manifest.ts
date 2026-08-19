import type { MetadataRoute } from "next";

export default function manifest(): MetadataRoute.Manifest {
  return {
    name: "[PRODUCT_NAME]",
    short_name: "[PRODUCT_NAME]",
    description: "Cuidado nutricional com continuidade.",
    start_url: "/",
    display: "standalone",
    background_color: "#f7f5ef",
    theme_color: "#173f3b",
    lang: "pt-BR",
  };
}
