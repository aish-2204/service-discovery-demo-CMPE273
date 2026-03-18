package demo.backend.config;

import java.util.Objects;
import java.util.UUID;

public class BackendConfig {
    private static final String DEFAULT_SERVICE_NAME = "backend-service";
    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 9101;
    private static final String DEFAULT_REGISTRY_URL = "http://localhost:9000";
    private static final long DEFAULT_HEARTBEAT_INTERVAL_MS = 5000;

    private final String serviceName;
    private final String host;
    private final int port;
    private final String registryUrl;
    private final long heartbeatIntervalMs;
    private final String instanceId;

    private BackendConfig(String serviceName, String host, int port, String registryUrl, long heartbeatIntervalMs, String instanceId) {
        this.serviceName = serviceName;
        this.host = host;
        this.port = port;
        this.registryUrl = registryUrl;
        this.heartbeatIntervalMs = heartbeatIntervalMs;
        this.instanceId = instanceId;
    }

    public static BackendConfig fromEnv() {
        String serviceName = getenvOrDefault("SERVICE_NAME", DEFAULT_SERVICE_NAME);
        String host = getenvOrDefault("HOST", DEFAULT_HOST);
        int port = Integer.parseInt(getenvOrDefault("PORT", String.valueOf(DEFAULT_PORT)));
        String registryUrl = getenvOrDefault("REGISTRY_URL", DEFAULT_REGISTRY_URL);
        long heartbeatIntervalMs = Long.parseLong(getenvOrDefault("HEARTBEAT_INTERVAL_MS", String.valueOf(DEFAULT_HEARTBEAT_INTERVAL_MS)));
        String instanceId = getenvOrDefault("INSTANCE_ID", UUID.randomUUID().toString());
        return new BackendConfig(serviceName, host, port, registryUrl, heartbeatIntervalMs, instanceId);
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getRegistryUrl() {
        return registryUrl;
    }

    public long getHeartbeatIntervalMs() {
        return heartbeatIntervalMs;
    }

    public String getInstanceId() {
        return instanceId;
    }

    private static String getenvOrDefault(String name, String fallback) {
        return Objects.requireNonNullElse(System.getenv(name), fallback);
    }
}

