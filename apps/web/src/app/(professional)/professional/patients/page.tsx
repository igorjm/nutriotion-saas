import { CircleAlert, Clock3, Users } from "lucide-react";
import Link from "next/link";
import { ProfessionalSidebar } from "@/components/professional-sidebar";
import { getPatients } from "@/lib/api/server";
import { InvitePatientForm } from "./invite-patient-form";

export const dynamic = "force-dynamic";

const statusLabels: Record<string, string> = {
  INVITED: "Convite pendente",
  ACTIVE: "Ativo",
  PAUSED: "Pausado",
  ARCHIVED: "Arquivado",
};

export default async function ProfessionalPatientsPage() {
  const patients = await getPatients();

  return (
    <main className="professional-shell">
      <ProfessionalSidebar active="patients" />
      <section className="professional-content">
        <header>
          <div><span className="eyebrow">Sprint 2</span><h1>Pacientes</h1></div>
          <span className="environment-badge">DESENVOLVIMENTO · DADOS FICTÍCIOS</span>
        </header>

        <div className="patients-layout">
          <section className="patient-list-card" aria-labelledby="patients-title">
            <div className="section-card-heading compact-heading">
              <span className="foundation-icon"><Users /></span>
              <div>
                <span className="eyebrow">Vínculos da organização</span>
                <h2 id="patients-title">Lista de pacientes</h2>
                <p>Convites pendentes aparecem antes dos vínculos ativos.</p>
              </div>
            </div>

            {patients === null ? (
              <div className="patient-list-state error-state">
                <CircleAlert />
                <div><strong>Não foi possível carregar a lista.</strong><p>Confirme se a API local está disponível e tente novamente.</p></div>
              </div>
            ) : patients.length === 0 ? (
              <div className="patient-list-state">
                <Users />
                <div><strong>Nenhum paciente ainda.</strong><p>Crie o primeiro convite fictício ao lado.</p></div>
              </div>
            ) : (
              <div className="patient-list" aria-label="Pacientes da organização">
                <div className="patient-list-row patient-list-header">
                  <span>Paciente</span><span>Foco inicial</span><span>Status</span>
                </div>
                {patients.map((patient) => {
                  const content = <>
                    <span className="patient-list-identity">
                      <i>{patient.displayName.split(" ").slice(0, 2).map((part) => part[0]).join("")}</i>
                      <span><strong>{patient.displayName}</strong><small>{patient.contactEmail ?? "Contato ainda não informado"}</small></span>
                    </span>
                    <span>{patient.careFocus ?? "A definir no onboarding"}</span>
                    <span className={`relationship-status ${patient.relationshipStatus.toLowerCase()}`}>
                      <Clock3 /> {statusLabels[patient.relationshipStatus] ?? patient.relationshipStatus}
                    </span>
                  </>;
                  return patient.relationshipStatus === "ACTIVE" ? (
                    <Link className="patient-list-row patient-list-link" href={`/professional/patients/${patient.id}`} key={patient.id}>
                      {content}
                    </Link>
                  ) : (
                    <div className="patient-list-row" key={patient.id}>{content}</div>
                  );
                })}
              </div>
            )}
          </section>

          <InvitePatientForm />
        </div>
      </section>
    </main>
  );
}
