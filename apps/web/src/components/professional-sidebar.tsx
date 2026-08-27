import Link from "next/link";
import { Activity, ShieldCheck, Users } from "lucide-react";

type ProfessionalSidebarProps = {
  active: "foundation" | "patients";
};

export function ProfessionalSidebar({ active }: ProfessionalSidebarProps) {
  return (
    <aside className="professional-sidebar">
      <Link className="brand inverted" href="/">
        <span className="brand-mark" />
        <strong>[PRODUCT_NAME]</strong>
      </Link>
      <nav aria-label="Navegação profissional">
        <Link className={active === "foundation" ? "active" : undefined} href="/professional">
          <Activity /> Fundação
        </Link>
        <Link className={active === "patients" ? "active" : undefined} href="/professional/patients">
          <Users /> Pacientes
        </Link>
      </nav>
      <div className="security-note">
        <ShieldCheck />
        <span>Ambiente de desenvolvimento<br /><small>Somente dados fictícios</small></span>
      </div>
    </aside>
  );
}
