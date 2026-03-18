package demo.registry.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import demo.registry.store.InstanceRecord;
import demo.registry.store.RegistryStore;
import demo.registry.util.HttpUtils;
import demo.registry.util.JsonUtils;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;

public class ServicesHandler implements HttpHandler {
    private final RegistryStore store;

    public ServicesHandler(RegistryStore store) {
        this.store = store;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
            HttpUtils.sendResponse(exchange, 405, "Method Not Allowed");
            return;
        }

        URI uri = exchange.getRequestURI();
        String path = uri.getPath();
        String[] parts = path.split("/");

        if (parts.length == 2) {
            Map<String, List<InstanceRecord>> allHealthy = store.allHealthy();
            HttpUtils.sendJson(exchange, 200, JsonUtils.buildAllServicesJson(allHealthy));
            return;
        }

        if (parts.length == 3) {
            String serviceName = parts[2];
            List<InstanceRecord> healthy = store.healthyInstances(serviceName);
            HttpUtils.sendJson(exchange, 200, JsonUtils.buildServiceJson(healthy));
            return;
        }

        HttpUtils.sendResponse(exchange, 404, "Not Found");
    }
}

