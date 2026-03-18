package demo.registry.config;

import java.util.Objects;

public class RegistryConfig {
    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 9000;
    private static final long DEFAULT_TTL_MS = 10000;
    private static final long DEFAULT_CLEANUP_INTERVAL_MS = 5000;

    private final String host;
    private final int port;
    private final long ttlMs;
    private final long cleanupIntervalMs;

    private RegistryConfig(String host, int port, long ttlMs, long cleanupIntervalMs) {
        this.host = host;
        this.port = port;
        this.ttlMs = ttlMs;
        this.cleanupIntervalMs = cleanupIntervalMs;
    }

    public static RegistryConfig fromEnv() {
        String host = getenvOrDefault("REGISTRY_HOST", DEFAULT_HOST);
        int port = Integer.parseInt(getenvOrDefault("REGISTRY_PORT", String.valueOf(DEFAULT_PORT)));
        long ttlMs = Long.parseLong(getenvOrDefault("REGISTRY_TTL_MS", String.valueOf(DEFAULT_TTL_MS)));
        long cleanupIntervalMs = Long.parseLong(getenvOrDefault("REGISTRY_CLEANUP_INTERVAL_MS", String.valueOf(DEFAULT_CLEANUP_INTERVAL_MS)));
        return new RegistryConfig(host, port, ttlMs, cleanupIntervalMs);
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public long getTtlMs() {
        return ttlMs;
    }

    public long getCleanupIntervalMs() {
        return cleanupIntervalMs;
    }

    private static String getenvOrDefault(String name, String fallback) {
        return Objects.requireNonNullElse(System.getenv(name), fallback);
    }
}

