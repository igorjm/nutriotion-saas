import Link from "next/link";
import { Activity, ArrowRight, Building2, CheckCircle2, ShieldCheck, UserPlus } from "lucide-react";
import { getSessionContext } from "@/lib/api/server";
import { signOut } from "@/app/auth/login/actions";
import { ProfessionalSidebar } from "@/components/professional-sidebar";

export const dynamic = "force-dynamic";

export default async function ProfessionalHome() {
  const context = await getSessionContext();

  return (
    <main className="professional-shell">
      <ProfessionalSidebar active="foundation" />
      <section className="professional-content">
        <header>
          <div><span className="eyebrow">Sprint 1</span><h1>Fundação da prática</h1></div>
          <div className="professional-actions">
            <span className="environment-badge">STAGING · DADOS FICTÍCIOS</span>
            <form action={signOut}><button className="text-button" type="submit">Sair</button></form>
          </div>
        </header>

        {!context ? (
          <section className="auth-foundation-card">
            <span className="foundation-icon"><ShieldCheck /></span>
            <div>
              <span className="eyebrow">Identidade protegida</span>
              <h2>Sessão confirmada; contexto da prática ainda indisponível.</h2>
              <p>
                O acesso foi validado, mas a API ainda não encontrou uma associação ativa com uma
                Organization ou está despertando no ambiente gratuito.
              </p>
            </div>
            <Link className="button primary" href="/professional">Tentar novamente <ArrowRight size={17} /></Link>
          </section>
        ) : (
          <section className="auth-foundation-card connected">
            <span className="foundation-icon"><CheckCircle2 /></span>
            <div>
              <span className="eyebrow">Contexto resolvido pelo servidor</span>
              <h2>{context.displayName}</h2>
              <p>{context.organizationName} · {context.role}</p>
            </div>
          </section>
        )}

        <section className="foundation-grid">
          <article>
            <span><Building2 /></span>
            <small>Limite de acesso</small>
            <h3>Organização desde o primeiro usuário</h3>
            <p>O tenant é resolvido pela associação autenticada, nunca por um identificador confiado ao navegador.</p>
          </article>
          <article>
            <span><UserPlus /></span>
            <small>Fatia vertical disponível</small>
            <h3>Convide um paciente fictício</h3>
            <p>Teste convite, criação da conta do paciente, consentimento e ativação do vínculo.</p>
            <Link className="text-link card-link" href="/professional/patients">Abrir pacientes <ArrowRight size={15} /></Link>
          </article>
          <article>
            <span><Activity /></span>
            <small>Operação</small>
            <h3>Saúde e rastreabilidade</h3>
            <p>API, banco e migrações expõem sinais de saúde sem registrar nomes, notas ou conteúdo clínico em logs.</p>
          </article>
        </section>
      </section>
    </main>
  );
}
