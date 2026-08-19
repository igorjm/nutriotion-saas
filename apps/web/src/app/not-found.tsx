import Link from "next/link";

export default function NotFound() {
  return (
    <main className="state-page">
      <div className="state-card">
        <span className="eyebrow">Página não encontrada</span>
        <h1>Este caminho não existe.</h1>
        <p>Volte para o início e continue explorando a plataforma.</p>
        <Link className="button primary" href="/">Voltar ao início</Link>
      </div>
    </main>
  );
}
