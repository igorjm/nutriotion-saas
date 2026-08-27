#!/usr/bin/env bash
set -euo pipefail

repository_root="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
supabase_version="2.115.0"
local_email="${LOCAL_AUTH_EMAIL:-mariana.local@example.invalid}"
local_password="${LOCAL_AUTH_PASSWORD:-LocalOnly!2026}"

case "$local_email" in
  *.invalid) ;;
  *)
    echo "Local auth email must use the reserved .invalid domain." >&2
    exit 1
    ;;
esac

for command_name in curl jq npx; do
  if ! command -v "$command_name" >/dev/null 2>&1; then
    echo "Missing required command: $command_name" >&2
    exit 1
  fi
done

status_json="$(npx --yes "supabase@$supabase_version" status -o json --workdir "$repository_root")"
api_url="$(jq -r '.API_URL' <<<"$status_json")"
service_role_key="$(jq -r '.SERVICE_ROLE_KEY' <<<"$status_json")"

case "$api_url" in
  http://127.0.0.1:*|http://localhost:*) ;;
  *)
    echo "Refusing to bootstrap auth outside localhost: $api_url" >&2
    exit 1
    ;;
esac

auth_headers=(
  -H "apikey: $service_role_key"
  -H "Authorization: Bearer $service_role_key"
  -H "Content-Type: application/json"
)

users_json="$(curl -fsS "$api_url/auth/v1/admin/users?page=1&per_page=1000" "${auth_headers[@]}")"
user_id="$(jq -r --arg email "$local_email" '.users[] | select((.email | ascii_downcase) == ($email | ascii_downcase)) | .id' <<<"$users_json" | head -n 1)"
payload="$(jq -n \
  --arg email "$local_email" \
  --arg password "$local_password" \
  '{email: $email, password: $password, email_confirm: true}')"

if [[ -n "$user_id" ]]; then
  user_json="$(curl -fsS -X PUT "$api_url/auth/v1/admin/users/$user_id" \
    "${auth_headers[@]}" \
    --data "$payload")"
else
  user_json="$(curl -fsS -X POST "$api_url/auth/v1/admin/users" \
    "${auth_headers[@]}" \
    --data "$payload")"
fi

created_id="$(jq -r '.id // empty' <<<"$user_json")"
confirmed_at="$(jq -r '.email_confirmed_at // empty' <<<"$user_json")"

if [[ -z "$created_id" || -z "$confirmed_at" ]]; then
  echo "Local Auth user was not created and confirmed as expected." >&2
  exit 1
fi

echo "Local Auth fixture is ready."
echo "Email: $local_email"
echo "Password: $local_password"
echo "Scope: localhost only"
