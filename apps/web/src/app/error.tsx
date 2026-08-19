"use client";

export default function GlobalError({ reset }: { error: Error & { digest?: string }; reset: () => void }) {
  return (
    <main className="state-page">
      <div className="state-card">
        <span className="eyebrow">Não foi possível continuar</span>
        <h1>Algo saiu do esperado.</h1>
        <p>Nenhum dado foi perdido. Tente novamente ou volte em alguns instantes.</p>
        <button className="button primary" onClick={reset}>Tentar novamente</button>
      </div>
    </main>
  );
}
