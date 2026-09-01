import { ArrowLeft, AtSign, HeartHandshake, ShieldCheck } from "lucide-react";
import Link from "next/link";
import { notFound } from "next/navigation";
import { ProfessionalSidebar } from "@/components/professional-sidebar";
import { getPatientClinicalRecord } from "@/lib/api/server";
import { ConsultationEditor } from "./consultation-editor";
import { IntakeForm } from "./intake-form";
import { StartConsultation } from "./start-consultation";

export const dynamic = "force-dynamic";

type PatientClinicalPageProps = {
  params: Promise<{ patientId: string }>;
};

export default async function PatientClinicalPage({ params }: PatientClinicalPageProps) {
  const { patientId } = await params;
  const record = await getPatientClinicalRecord(patientId);
  if (!record) notFound();

  return (
    <main className="professional-shell">
      <ProfessionalSidebar active="patients" />
      <section className="professional-content clinical-record-page">
        <header>
          <div>
            <Link className="back-link" href="/professional/patients"><ArrowLeft size={15} /> Voltar para pacientes</Link>
            <span className="eyebrow">Sprint 2 · Patient 360</span>
            <h1>{record.displayName}</h1>
          </div>
          <span className="environment-badge">DESENVOLVIMENTO · DADOS FICTÍCIOS</span>
        </header>

        <section className="patient-overview" aria-label="Resumo do paciente">
          <div className="patient-avatar">{record.displayName.split(" ").slice(0, 2).map((part) => part[0]).join("")}</div>
          <div><AtSign /><span><small>Contato</small><strong title={record.contactEmail ?? undefined}>{record.contactEmail ?? "Ainda não informado"}</strong></span></div>
          <div><HeartHandshake /><span><small>Foco do cuidado</small><strong>{record.careFocus ?? "A definir"}</strong></span></div>
          <div><ShieldCheck /><span><small>Vínculo</small><strong>Ativo e isolado por organização</strong></span></div>
        </section>

        <div className="clinical-record-layout">
          <IntakeForm patientId={patientId} intake={record.intake} />
          {record.consultation ? (
            <ConsultationEditor patientId={patientId} initialWorkspace={record.consultation} />
          ) : (
            <StartConsultation patientId={patientId} />
          )}
        </div>
      </section>
    </main>
  );
}
