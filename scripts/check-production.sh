#!/usr/bin/env bash
set -euo pipefail

repository_root="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"

npm --prefix "$repository_root/packages/api-client" run check
npm --prefix "$repository_root/apps/web" run check
"$repository_root/services/api/mvnw" -f "$repository_root/services/api/pom.xml" verify
