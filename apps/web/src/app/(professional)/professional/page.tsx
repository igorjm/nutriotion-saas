import Link from "next/link";
import { Activity, ArrowRight, Building2, CheckCircle2, ShieldCheck, UserPlus, Users } from "lucide-react";
import { getSessionContext } from "@/lib/api/server";

export const dynamic = "force-dynamic";

export default async function ProfessionalHome() {
  const context = await getSessionContext();

  return (
    <main className="professional-shell">
      <aside className="professional-sidebar">
        <Link className="brand inverted" href="/"><span className="brand-mark" /><strong>[PRODUCT_NAME]</strong></Link>
        <nav aria-label="Navegação profissional">
          <span className="active"><Activity /> Fundação</span>
          <span><Users /> Pacientes</span>
        </nav>
        <div className="security-note"><ShieldCheck /><span>Ambiente de fundação<br /><small>Sem dados clínicos reais</small></span></div>
      </aside>
      <section className="professional-content">
        <header>
          <div><span className="eyebrow">Sprint 0</span><h1>Fundação da prática</h1></div>
          <span className="environment-badge">STAGING · DADOS FICTÍCIOS</span>
        </header>

        {!context ? (
          <section className="auth-foundation-card">
            <span className="foundation-icon"><ShieldCheck /></span>
            <div>
              <span className="eyebrow">Identidade protegida</span>
              <h2>A estrutura está pronta para conectar seu acesso.</h2>
              <p>
                O shell de produção já separa identidade, organização e relacionamento com pacientes.
                Configure o emissor OIDC para validar uma sessão real.
              </p>
            </div>
            <Link className="button primary" href="/auth/callback">Ver estado da autenticação <ArrowRight size={17} /></Link>
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
            <h3>Organization desde o primeiro usuário</h3>
            <p>O tenant é resolvido pela associação autenticada, nunca por um identificador confiado ao navegador.</p>
          </article>
          <article>
            <span><UserPlus /></span>
            <small>Próxima fatia vertical</small>
            <h3>Convite e vínculo com o paciente</h3>
            <p>Convite, consentimento e relacionamento serão entregues juntos, com auditoria e testes negativos.</p>
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
