import Link from "next/link";
import { CheckCircle2, KeyRound, LockKeyhole, MailCheck, ShieldCheck, UserRound } from "lucide-react";
import { BrandLockup } from "@/components/brand-lockup";
import { getPatientInvitationPreview } from "@/lib/api/server";
import { createClient } from "@/lib/supabase/server";
import { acceptInvitation, createInvitedPatientAccount, signOutFromInvitation } from "./actions";

type InvitationPageProps = {
  params: Promise<{ token: string }>;
  searchParams: Promise<{ status?: string }>;
};

const messages: Record<string, string> = {
  "signup-error": "Não foi possível criar a conta. Confira o e-mail, use uma senha com pelo menos oito caracteres ou entre com uma conta existente.",
  "confirmation-sent": "Enviamos a confirmação para o e-mail informado. Depois de confirmar, você voltará para este convite.",
  "account-ready": "Conta criada. Agora revise o consentimento para ativar o vínculo.",
  "consent-required": "Leia e aceite o consentimento para continuar.",
  "accept-error": "Não foi possível ativar o vínculo. Confirme se esta conta usa o mesmo e-mail do convite.",
};

export default async function PatientInvitationPage({ params, searchParams }: InvitationPageProps) {
  const { token } = await params;
  const { status } = await searchParams;
  const preview = await getPatientInvitationPreview(token);
  const supabase = await createClient();
  const { data: claimsData } = await supabase.auth.getClaims();
  const claims = claimsData?.claims;
  const signedInEmail = typeof claims?.email === "string" ? claims.email : null;

  if (!preview) {
    return (
      <main className="invitation-page">
        <section className="invitation-card invitation-unavailable">
          <LockKeyhole />
          <span className="eyebrow">Convite indisponível</span>
          <h1>Este link não é válido ou não está mais disponível.</h1>
          <p>Peça à nutricionista responsável para criar um novo convite.</p>
          <Link className="button secondary" href="/">Voltar ao início</Link>
        </section>
      </main>
    );
  }

  const isPending = preview.status === "PENDING";

  return (
    <main className="invitation-page">
      <section className="invitation-intro">
        <BrandLockup className="inverted" showPromise />
        <div>
          <span className="eyebrow">Convite para acompanhamento</span>
          <h1>{preview.organizationName} convidou você.</h1>
          <p>
            Crie sua conta, confirme seu contato e escolha se deseja ativar este vínculo de cuidado.
            Nenhuma autorização de marketing é exigida.
          </p>
        </div>
        <div className="invitation-trust">
          <span><ShieldCheck /> Consentimento claro e versionado</span>
          <span><LockKeyhole /> Convite protegido por link individual</span>
          <span><UserRound /> A nutricionista continua responsável pelo cuidado</span>
        </div>
        <small>DESENVOLVIMENTO · use somente dados fictícios</small>
      </section>

      <section className="invitation-panel" aria-labelledby="invitation-title">
        <div className="invited-person">
          <i>{preview.patientDisplayName.split(" ").slice(0, 2).map((part) => part[0]).join("")}</i>
          <div>
            <span className="eyebrow">Convite para</span>
            <h2 id="invitation-title">{preview.patientDisplayName}</h2>
            <p>{preview.maskedEmail}</p>
          </div>
        </div>

        {status && messages[status] ? <p className={`auth-message ${status.includes("error") ? "error" : ""}`} role="status">{messages[status]}</p> : null}

        {!isPending ? (
          <div className="invitation-complete">
            <CheckCircle2 />
            <h3>{preview.status === "ACCEPTED" ? "Este convite já foi aceito." : "Este convite expirou ou foi encerrado."}</h3>
            <p>{preview.status === "ACCEPTED" ? "O vínculo de cuidado já está ativo." : "Solicite um novo link à nutricionista."}</p>
            {preview.status === "ACCEPTED" && claims ? <Link className="button primary" href="/patient/welcome">Continuar</Link> : null}
          </div>
        ) : !claims ? (
          <form className="invitation-form" action={createInvitedPatientAccount}>
            <input type="hidden" name="token" value={token} />
            <div className="form-section-title"><KeyRound /><div><strong>Criar conta do paciente</strong><p>Use exatamente o e-mail que recebeu o convite.</p></div></div>
            <label>
              E-mail
              <input name="email" type="email" autoComplete="email" required />
            </label>
            <label>
              Criar senha
              <input name="password" type="password" autoComplete="new-password" minLength={8} required />
            </label>
            <button className="button primary" type="submit"><MailCheck size={17} /> Criar conta e confirmar contato</button>
            <Link className="button secondary" href={`/auth/login?next=${encodeURIComponent(`/convite/${token}`)}`}>
              Já tenho uma conta
            </Link>
          </form>
        ) : (
          <form className="consent-review" action={acceptInvitation}>
            <input type="hidden" name="token" value={token} />
            <input type="hidden" name="consentTextVersion" value={preview.consentTextVersion} />
            <div className="signed-in-identity">
              <MailCheck />
              <div><small>Conta conectada</small><strong>{signedInEmail ?? "E-mail confirmado pelo provedor"}</strong></div>
              <button className="text-button" type="submit" formAction={signOutFromInvitation} formNoValidate>Usar outra conta</button>
            </div>
            <div className="consent-copy">
              <span className="eyebrow">Consentimento · versão {preview.consentTextVersion}</span>
              <h3>Ativar o vínculo com {preview.organizationName}</h3>
              {preview.consentText.split("\n\n").map((paragraph) => <p key={paragraph}>{paragraph}</p>)}
            </div>
            <label className="consent-checkbox">
              <input name="consent" type="checkbox" value="accepted" required />
              <span>Li as informações acima e desejo ativar este vínculo de cuidado.</span>
            </label>
            <button className="button primary" type="submit"><CheckCircle2 size={17} /> Aceitar e ativar vínculo</button>
          </form>
        )}
      </section>
    </main>
  );
}
