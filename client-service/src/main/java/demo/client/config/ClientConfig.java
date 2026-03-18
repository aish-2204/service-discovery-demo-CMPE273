package demo.client.config;

import java.util.Objects;

public class ClientConfig {
    private static final String DEFAULT_BACKEND_SERVICE = "backend-service";
    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 9200;
    private static final String DEFAULT_REGISTRY_URL = "http://localhost:9000";

    private final String host;
    private final int port;
    private final String registryUrl;
    private final String backendServiceName;

    private ClientConfig(String host, int port, String registryUrl, String backendServiceName) {
        this.host = host;
        this.port = port;
        this.registryUrl = registryUrl;
        this.backendServiceName = backendServiceName;
    }

    public static ClientConfig fromEnv() {
        String host = getenvOrDefault("HOST", DEFAULT_HOST);
        int port = Integer.parseInt(getenvOrDefault("PORT", String.valueOf(DEFAULT_PORT)));
        String registryUrl = getenvOrDefault("REGISTRY_URL", DEFAULT_REGISTRY_URL);
        String backendServiceName = getenvOrDefault("BACKEND_SERVICE_NAME", DEFAULT_BACKEND_SERVICE);
        return new ClientConfig(host, port, registryUrl, backendServiceName);
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

    public String getBackendServiceName() {
        return backendServiceName;
    }

    private static String getenvOrDefault(String name, String fallback) {
        return Objects.requireNonNullElse(System.getenv(name), fallback);
    }
}

