import { Activity, ShieldCheck, Users } from "lucide-react";
import Link from "next/link";
import { BrandLockup } from "@/components/brand-lockup";

type ProfessionalSidebarProps = {
  active: "foundation" | "patients";
};

export function ProfessionalSidebar({ active }: ProfessionalSidebarProps) {
  return (
    <aside className="professional-sidebar">
      <BrandLockup className="inverted" />
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
