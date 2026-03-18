#!/usr/bin/env bash
set -euo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
PID_FILE="$ROOT/.demo-pids"

if [[ ! -f "$PID_FILE" ]]; then
  echo "No PID file found at $PID_FILE"
  exit 0
fi

while IFS= read -r line; do
  pid="${line%%:*}"
  name="${line#*:}"
  if kill "$pid" >/dev/null 2>&1; then
    echo "Stopped $name (pid $pid)"
  fi
done < "$PID_FILE"

rm -f "$PID_FILE"

