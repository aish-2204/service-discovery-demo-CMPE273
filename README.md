# Java Client-Side Service Discovery Demo

## Architecture (Brief)
- `registry` keeps an in-memory list of service instances and removes them if heartbeats stop.
- `backend-service` registers on startup, renews via heartbeats, and exposes `/api/data`.
- `client-service` queries the registry for healthy `backend-service` instances, picks one at random, and calls it via `/call-backend`.

## Architecture Diagram
<img width="1029" height="1194" alt="image" src="https://github.com/user-attachments/assets/7c6b74ce-e3a8-44cc-84d8-010bc3452be0" />

## Folder Structure
```
registry/
  src/main/java/demo/registry/
    cleanup/
    config/
    http/
    store/
    util/
backend-service/
  src/main/java/demo/backend/
    config/
    http/
    registry/
    util/
client-service/
  src/main/java/demo/client/
    backend/
    config/
    http/
    model/
    registry/
    util/
scripts/
```

## Environment Variables
Common variables:
- `REGISTRY_URL` (e.g., `http://localhost:9000`)
- `SERVICE_NAME` (used by backend)
- `HOST` (bind host or advertised host)
- `PORT` (service port)
- `INSTANCE_ID` (unique ID for backend instances)

Registry variables:
- `REGISTRY_HOST`, `REGISTRY_PORT`
- `REGISTRY_TTL_MS` (default 15000)
- `REGISTRY_CLEANUP_INTERVAL_MS` (default 5000)

Backend variables:
- `HEARTBEAT_INTERVAL_MS` (default 5000)

Client variables:
- `BACKEND_SERVICE_NAME` (default `backend-service`)

## Run Instructions (Local)
1. Build once (optional but recommended):
   ```bash
   mvn -q -DskipTests package
   ```
2. Start everything with the demo script:
   ```bash
   scripts/run-demo.sh
   ```
3. Stop everything:
   ```bash
   scripts/stop-demo.sh
   ```

## Run Instructions (Docker Compose)
```bash
docker compose up
```

Stop:
```bash
docker compose down
```

## Verification
### Confirm registry has both backend instances
- UI: `http://localhost:9000/ui`
- API: `http://localhost:9000/services/backend-service`

Example response:
```json
[
  {"serviceName":"backend-service","instanceId":"backend-1","host":"localhost","port":9101},
  {"serviceName":"backend-service","instanceId":"backend-2","host":"localhost","port":9102}
]
```

### Call the client multiple times
```bash
scripts/smoke-test.sh
```

If you are using Docker Compose and want the script to target it explicitly:
```bash
REGISTRY_URL=http://localhost:9000 CLIENT_URL=http://localhost:9200 scripts/smoke-test.sh
```

Example output (notice different instance IDs):
```json
{"selectedInstance":{"instanceId":"backend-1","host":"backend-1","port":9101},"backendResponse":{"instanceId":"backend-1","host":"backend-1","port":9101,"timestamp":"2026-03-18T10:00:00Z"},"timestamp":"2026-03-18T10:00:01Z"}
{"selectedInstance":{"instanceId":"backend-2","host":"backend-2","port":9102},"backendResponse":{"instanceId":"backend-2","host":"backend-2","port":9102,"timestamp":"2026-03-18T10:00:02Z"},"timestamp":"2026-03-18T10:00:03Z"}
```

## Manual Run (if you prefer)
Registry:
```bash
env REGISTRY_HOST=localhost REGISTRY_PORT=9000 mvn -q -pl registry exec:java
```

Backend instance 1:
```bash
env SERVICE_NAME=backend-service HOST=localhost PORT=9101 INSTANCE_ID=backend-1 REGISTRY_URL=http://localhost:9000 mvn -q -pl backend-service exec:java
```

Backend instance 2:
```bash
env SERVICE_NAME=backend-service HOST=localhost PORT=9102 INSTANCE_ID=backend-2 REGISTRY_URL=http://localhost:9000 mvn -q -pl backend-service exec:java
```

Client:
```bash
env HOST=localhost PORT=9200 REGISTRY_URL=http://localhost:9000 BACKEND_SERVICE_NAME=backend-service mvn -q -pl client-service exec:java
```
