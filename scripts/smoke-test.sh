#!/usr/bin/env bash
set -euo pipefail

CLIENT_URL="${CLIENT_URL:-http://localhost:9200}"
REGISTRY_URL="${REGISTRY_URL:-http://localhost:9000}"
SERVICE_NAME="${SERVICE_NAME:-backend-service}"

echo "Registry instances:"
curl -s "$REGISTRY_URL/services/$SERVICE_NAME" | cat
echo ""

echo ""
for i in {1..5}; do
  echo "Call $i:"
  curl -s "$CLIENT_URL/call-backend" | cat
  echo ""
  sleep 0.5
done
