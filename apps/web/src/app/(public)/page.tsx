import Link from "next/link";
import {
  ArrowRight,
  CheckCircle2,
  ClipboardList,
  HeartHandshake,
  ShieldCheck,
  Sparkles,
  TrendingUp,
  Users,
} from "lucide-react";
import { EarlyAccessForm } from "@/components/early-access-form";

const benefits = [
  {
    icon: ClipboardList,
    title: "Da consulta ao plano, sem perder contexto",
    copy: "Histórico, decisões, versões e acompanhamento organizados em um fluxo contínuo.",
  },
  {
    icon: HeartHandshake,
    title: "Uma experiência simples para o paciente",
    copy: "O que fazer hoje, trocas aprovadas e registros rápidos, sem transformar cuidado em cobrança.",
  },
  {
    icon: TrendingUp,
    title: "Crescimento também faz parte da prática",
    copy: "Planeje conteúdo ético e organize novos contatos sem montar um segundo sistema.",
  },
];

export default function LandingPage() {
  return (
    <main className="marketing-shell">
      <header className="marketing-header">
        <Link className="brand" href="/" aria-label="Página inicial">
          <span className="brand-mark" aria-hidden="true" />
          <strong>[PRODUCT_NAME]</strong>
        </Link>
        <nav aria-label="Navegação pública">
          <a href="#produto">Produto</a>
          <a href="#seguranca">Segurança</a>
          <a href="#acesso">Acesso inicial</a>
        </nav>
        <Link className="button secondary" href="/professional">Entrar</Link>
      </header>

      <section className="hero" id="produto">
        <div className="hero-copy">
          <span className="hero-pill"><Users size={15} /> Feito com nutricionistas, para a prática real</span>
          <h1>Mais tempo para cuidar. <em>Mais clareza para crescer.</em></h1>
          <p>
            Uma plataforma brasileira para organizar o consultório, acompanhar pacientes e reduzir
            retrabalho sem tirar o nutricionista do controle.
          </p>
          <div className="hero-actions">
            <a className="button primary" href="#acesso">Participar do acesso inicial <ArrowRight size={17} /></a>
            <a className="text-link" href="#como-funciona">Conhecer a proposta</a>
          </div>
          <div className="trust-line">
            <span><CheckCircle2 size={16} /> Pequeno grupo piloto</span>
            <span><CheckCircle2 size={16} /> Construído no Brasil</span>
            <span><CheckCircle2 size={16} /> Sem cobrança automática</span>
          </div>
        </div>

        <div className="product-preview" aria-label="Prévia do produto">
          <div className="preview-top"><span /><span /><span /><small>Seu consultório hoje</small></div>
          <div className="preview-body">
            <aside>
              <strong>[PRODUCT_NAME]</strong>
              {['Hoje', 'Pacientes', 'Consultas', 'Planos', 'Crescimento'].map((item, index) => (
                <span className={index === 0 ? "active" : ""} key={item}>{item}</span>
              ))}
            </aside>
            <section>
              <small>QUARTA-FEIRA</small>
              <h2>Bom dia, Mariana.</h2>
              <div className="preview-metrics">
                <div><strong>84</strong><span>pacientes ativos</span></div>
                <div><strong>4</strong><span>consultas hoje</span></div>
                <div><strong>3</strong><span>planos para revisar</span></div>
              </div>
              <article className="preview-consultation">
                <div><small>PRÓXIMA CONSULTA · 14:00</small><strong>Camila Ribeiro</strong></div>
                <span className="ai-label"><Sparkles size={14} /> Resumo pronto para revisar</span>
                <p>Dificuldade recorrente no lanche da tarde e uma nova dúvida sobre pré-treino.</p>
                <span className="preview-button">Preparar consulta</span>
              </article>
            </section>
          </div>
        </div>
      </section>

      <section className="benefits-section" id="como-funciona">
        <span className="eyebrow">Um ciclo de cuidado conectado</span>
        <h2>Menos tarefas espalhadas. Mais continuidade entre consultas.</h2>
        <div className="benefit-grid">
          {benefits.map(({ icon: Icon, title, copy }) => (
            <article key={title}>
              <span><Icon aria-hidden="true" /></span>
              <h3>{title}</h3>
              <p>{copy}</p>
            </article>
          ))}
        </div>
      </section>

      <section className="safety-section" id="seguranca">
        <div>
          <span className="eyebrow">Tecnologia responsável</span>
          <h2>IA como copiloto. O profissional continua no comando.</h2>
          <p>
            Resumos e sugestões são identificados, rastreáveis e sujeitos à revisão. Nenhuma mudança
            clínica é publicada silenciosamente.
          </p>
          <ul>
            <li><ShieldCheck /> Organização e relacionamento definem o acesso</li>
            <li><ShieldCheck /> Consentimentos e alterações importantes deixam histórico</li>
            <li><ShieldCheck /> Dados clínicos não entram em analytics ou logs comuns</li>
          </ul>
        </div>
        <div className="approval-flow">
          <article><Sparkles /><span><small>A assistência prepara</small><strong>Um rascunho contextual</strong></span></article>
          <i />
          <article className="selected"><HeartHandshake /><span><small>O nutricionista revisa</small><strong>Ajusta e aprova</strong></span></article>
          <i />
          <article><CheckCircle2 /><span><small>O paciente recebe</small><strong>Somente o conteúdo publicado</strong></span></article>
        </div>
      </section>

      <section className="access-section" id="acesso">
        <div>
          <span className="eyebrow">Acesso inicial</span>
          <h2>Ajude a construir a ferramenta que você gostaria de usar todos os dias.</h2>
          <p>
            Estamos conversando com nutricionistas que usam outras plataformas e querem uma migração
            segura, fluxos mais rápidos e uma experiência melhor para seus pacientes.
          </p>
        </div>
        <EarlyAccessForm />
      </section>

      <footer>
        <span className="brand"><span className="brand-mark" aria-hidden="true" /><strong>[PRODUCT_NAME]</strong></span>
        <p>Plataforma brasileira para nutricionistas e pacientes.</p>
        <small>Produto em validação · 2026</small>
      </footer>
    </main>
  );
}
