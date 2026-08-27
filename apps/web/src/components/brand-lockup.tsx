import { brand } from "@nutrition-platform/brand";
import Link from "next/link";

type BrandLockupProps = {
  className?: string;
  href?: string;
  showPromise?: boolean;
};

export function BrandLockup({ className, href = "/", showPromise = false }: BrandLockupProps) {
  return (
    <Link
      aria-label={`${brand.name} — página inicial`}
      className={["brand", className].filter(Boolean).join(" ")}
      href={href}
    >
      <span className="brand-mark" aria-hidden="true" />
      <span className="brand-copy">
        <strong>{brand.name}</strong>
        {showPromise ? <small>{brand.promise}</small> : null}
      </span>
    </Link>
  );
}
