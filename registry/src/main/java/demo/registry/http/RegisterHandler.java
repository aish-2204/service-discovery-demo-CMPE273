package demo.registry.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import demo.registry.store.RegistryStore;
import demo.registry.util.HttpUtils;
import demo.registry.util.JsonUtils;

import java.io.IOException;
import java.util.Map;

public class RegisterHandler implements HttpHandler {
    private final RegistryStore store;

    public RegisterHandler(RegistryStore store) {
        this.store = store;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            HttpUtils.sendResponse(exchange, 405, "Method Not Allowed");
            return;
        }

        String body = HttpUtils.readBody(exchange.getRequestBody());
        Map<String, String> payload = JsonUtils.parseJsonObject(body);
        String serviceName = payload.get("serviceName");
        String instanceId = payload.get("instanceId");
        String host = payload.get("host");
        String portValue = payload.get("port");

        if (JsonUtils.isBlank(serviceName) || JsonUtils.isBlank(instanceId)
                || JsonUtils.isBlank(host) || JsonUtils.isBlank(portValue)) {
            HttpUtils.sendResponse(exchange, 400, "Missing required fields");
            return;
        }

        int port;
        try {
            port = Integer.parseInt(portValue);
        } catch (NumberFormatException ex) {
            HttpUtils.sendResponse(exchange, 400, "Invalid port");
            return;
        }

        store.register(serviceName, instanceId, host, port);
        HttpUtils.sendJson(exchange, 200, "{\"status\":\"registered\"}");
    }
}

