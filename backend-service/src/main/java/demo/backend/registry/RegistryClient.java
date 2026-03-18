package demo.backend.registry;

import demo.backend.config.BackendConfig;
import demo.backend.util.JsonUtils;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RegistryClient {
    private final BackendConfig config;
    private final HttpClient httpClient = HttpClient.newHttpClient();

    public RegistryClient(BackendConfig config) {
        this.config = config;
    }

    public void register() throws Exception {
        String payload = "{"
                + "\"serviceName\":\"" + JsonUtils.escape(config.getServiceName()) + "\","
                + "\"instanceId\":\"" + JsonUtils.escape(config.getInstanceId()) + "\","
                + "\"host\":\"" + JsonUtils.escape(config.getHost()) + "\","
                + "\"port\":" + config.getPort()
                + "}";
        sendPost(config.getRegistryUrl() + "/register", payload);
    }

    public void startHeartbeat() {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(() -> {
            try {
                String payload = "{"
                        + "\"serviceName\":\"" + JsonUtils.escape(config.getServiceName()) + "\","
                        + "\"instanceId\":\"" + JsonUtils.escape(config.getInstanceId()) + "\""
                        + "}";
                sendPost(config.getRegistryUrl() + "/heartbeat", payload);
            } catch (Exception ex) {
                System.err.println("Heartbeat failed: " + ex.getMessage());
            }
        }, config.getHeartbeatIntervalMs(), config.getHeartbeatIntervalMs(), TimeUnit.MILLISECONDS);
    }

    private void sendPost(String url, String payload) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(payload))
                .build();
        httpClient.send(request, HttpResponse.BodyHandlers.discarding());
    }
}

