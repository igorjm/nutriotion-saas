import { redirect } from "next/navigation";
import { CheckCircle2, ShieldCheck } from "lucide-react";
import { createClient } from "@/lib/supabase/server";

export const dynamic = "force-dynamic";

export default async function PatientWelcomePage() {
  const supabase = await createClient();
  const { data } = await supabase.auth.getClaims();
  if (!data?.claims) redirect("/auth/login?next=/patient/welcome");

  return (
    <main className="patient-welcome-page">
      <section className="patient-welcome-card">
        <span className="welcome-check"><CheckCircle2 /></span>
        <span className="eyebrow">Vínculo ativo</span>
        <h1>Seu acompanhamento foi conectado.</h1>
        <p>
          O consentimento foi registrado e a nutricionista já pode ver o vínculo ativo na lista da organização.
          Nenhum dado clínico real deve ser inserido neste ambiente.
        </p>
        <div className="welcome-next">
          <ShieldCheck />
          <div><strong>Próxima etapa do produto</strong><span>Anamnese guiada, preferências e notificações ainda serão adicionadas.</span></div>
        </div>
      </section>
    </main>
  );
}
