package demo.registry.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import demo.registry.store.RegistryStore;
import demo.registry.util.HttpUtils;
import demo.registry.util.JsonUtils;

import java.io.IOException;
import java.util.Map;

public class HeartbeatHandler implements HttpHandler {
    private final RegistryStore store;

    public HeartbeatHandler(RegistryStore store) {
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

        if (JsonUtils.isBlank(serviceName) || JsonUtils.isBlank(instanceId)) {
            HttpUtils.sendResponse(exchange, 400, "Missing required fields");
            return;
        }

        boolean renewed = store.renew(serviceName, instanceId);
        if (!renewed) {
            HttpUtils.sendResponse(exchange, 404, "Instance not found");
            return;
        }

        HttpUtils.sendJson(exchange, 200, "{\"status\":\"renewed\"}");
    }
}

