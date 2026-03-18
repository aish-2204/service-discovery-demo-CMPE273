package demo.client.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import demo.client.backend.BackendClient;
import demo.client.config.ClientConfig;
import demo.client.model.BackendResponse;
import demo.client.model.ClientResponse;
import demo.client.model.Instance;
import demo.client.registry.RegistryClient;
import demo.client.util.JsonUtils;

import java.io.IOException;
import java.util.List;
import java.util.Random;

public class ClientHandler implements HttpHandler {
    private final ClientConfig config;
    private final RegistryClient registryClient;
    private final BackendClient backendClient;
    private final Random random = new Random();

    public ClientHandler(ClientConfig config) {
        this.config = config;
        this.registryClient = new RegistryClient(config.getRegistryUrl());
        this.backendClient = new BackendClient();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
            JsonUtils.sendJson(exchange, 405, "{\"error\":\"Method Not Allowed\"}");
            return;
        }

        try {
            List<Instance> instances = registryClient.fetchInstances(config.getBackendServiceName());
            if (instances.isEmpty()) {
                JsonUtils.sendJson(exchange, 503, "{\"error\":\"No healthy backend instances\"}");
                return;
            }

            Instance chosen = instances.get(random.nextInt(instances.size()));
            BackendResponse backendResponse = backendClient.callBackend(chosen);
            ClientResponse response = ClientResponse.from(chosen, backendResponse);
            JsonUtils.sendJson(exchange, 200, response.toJson());
        } catch (Exception ex) {
            JsonUtils.sendJson(exchange, 502, "{\"error\":\"" + JsonUtils.escape(ex.getMessage()) + "\"}");
        }
    }
}

