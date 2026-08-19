import Link from "next/link";
import { ShieldCheck } from "lucide-react";

export default function AuthCallbackStatus() {
  return (
    <main className="state-page">
      <div className="state-card">
        <ShieldCheck className="state-icon" />
        <span className="eyebrow">Adapter de identidade</span>
        <h1>Emissor OIDC ainda não configurado.</h1>
        <p>
          A aplicação está pronta para validar JWTs e resolver a organização no backend. Nenhuma
          credencial ou identidade fictícia foi embutida no build de produção.
        </p>
        <Link className="button primary" href="/professional">Voltar à fundação</Link>
      </div>
    </main>
  );
}
