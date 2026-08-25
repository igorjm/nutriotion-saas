import Link from "next/link";
import { KeyRound, Mail, ShieldCheck, UserPlus } from "lucide-react";
import { isSupabaseConfigured } from "@/lib/supabase/config";
import { createProfessionalAccount, requestMagicLink, signInWithPassword } from "./actions";

type LoginPageProps = {
  searchParams: Promise<{ status?: string; next?: string }>;
};

const messages: Record<string, string> = {
  invalid: "Não foi possível confirmar esse acesso. Confira o e-mail e a senha.",
  unavailable: "O acesso está temporariamente indisponível. Tente novamente em alguns instantes.",
  "link-sent": "Se o e-mail estiver autorizado, enviaremos um link seguro de acesso.",
  "confirmation-sent": "Enviamos as instruções para confirmar seu e-mail e concluir o cadastro.",
  "signed-out": "Sua sessão foi encerrada com segurança.",
  "callback-error": "O link de acesso expirou ou já foi utilizado. Solicite um novo link.",
};

export default async function LoginPage({ searchParams }: LoginPageProps) {
  const { status, next = "/professional" } = await searchParams;
  const configured = isSupabaseConfigured();
  const message = status ? messages[status] : undefined;

  return (
    <main className="auth-page">
      <section className="auth-intro">
        <Link className="brand" href="/"><span className="brand-mark" /><strong>[PRODUCT_NAME]</strong></Link>
        <div>
          <span className="eyebrow">Acesso profissional</span>
          <h1>Sua prática protegida desde o primeiro acesso.</h1>
          <p>
            A identidade é confirmada pelo Supabase. Organização, permissões e vínculos com
            pacientes continuam sendo decididos no servidor da plataforma.
          </p>
        </div>
        <ul>
          <li><ShieldCheck /> Sessão validada por assinatura e expiração</li>
          <li><KeyRound /> MFA obrigatório antes de dados clínicos reais</li>
          <li><Mail /> E-mail verificado e recuperação auditável</li>
        </ul>
        <small>STAGING · use somente dados fictícios</small>
      </section>

      <section className="auth-panel" aria-labelledby="auth-title">
        <div>
          <span className="eyebrow">Bem-vindo</span>
          <h2 id="auth-title">Entre na sua conta</h2>
          <p>Use sua senha ou receba um link seguro por e-mail.</p>
        </div>

        {message ? <p className="auth-message" role="status">{message}</p> : null}
        {!configured ? (
          <p className="auth-message error" role="alert">
            A autenticação ainda não foi configurada neste ambiente.
          </p>
        ) : null}

        <form className="auth-form">
          <input type="hidden" name="next" value={next} />
          <label htmlFor="email">E-mail profissional</label>
          <input id="email" name="email" type="email" autoComplete="email" required />
          <label htmlFor="password">Senha</label>
          <input id="password" name="password" type="password" autoComplete="current-password" minLength={8} />
          <button className="button primary" formAction={signInWithPassword} disabled={!configured}>
            Entrar com senha
          </button>
          <button className="button secondary" formAction={requestMagicLink} formNoValidate disabled={!configured}>
            Receber link seguro
          </button>
          <button className="auth-create" formAction={createProfessionalAccount} disabled={!configured}>
            <UserPlus size={16} /> Criar conta profissional
          </button>
        </form>

        <p className="auth-privacy">
          Ao continuar, você confirma que leu os termos e o aviso de privacidade. Nenhum dado
          clínico deve ser inserido neste ambiente de fundação.
        </p>
      </section>
    </main>
  );
}
