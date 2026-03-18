#!/usr/bin/env bash
set -euo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
PID_FILE="$ROOT/.demo-pids"

: > "$PID_FILE"

start() {
  local name="$1"
  shift
  (cd "$ROOT" && "$@") &
  local pid=$!
  echo "$pid:$name" >> "$PID_FILE"
  echo "Started $name (pid $pid)"
}

start "registry" env REGISTRY_HOST=localhost REGISTRY_PORT=9000 mvn -q -pl registry exec:java
start "backend-1" env SERVICE_NAME=backend-service HOST=localhost PORT=9101 INSTANCE_ID=backend-1 REGISTRY_URL=http://localhost:9000 mvn -q -pl backend-service exec:java
start "backend-2" env SERVICE_NAME=backend-service HOST=localhost PORT=9102 INSTANCE_ID=backend-2 REGISTRY_URL=http://localhost:9000 mvn -q -pl backend-service exec:java
start "client" env HOST=localhost PORT=9200 REGISTRY_URL=http://localhost:9000 BACKEND_SERVICE_NAME=backend-service mvn -q -pl client-service exec:java

echo ""
echo "All services started. PID file: $PID_FILE"
echo "Stop all: scripts/stop-demo.sh"

