package demo.backend.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import demo.backend.config.BackendConfig;
import demo.backend.util.JsonUtils;

import java.io.IOException;
import java.time.Instant;

public class DataHandler implements HttpHandler {
    private final BackendConfig config;

    public DataHandler(BackendConfig config) {
        this.config = config;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
            JsonUtils.sendJson(exchange, 405, "{\"error\":\"Method Not Allowed\"}");
            return;
        }

        String json = "{"
                + "\"instanceId\":\"" + JsonUtils.escape(config.getInstanceId()) + "\","
                + "\"host\":\"" + JsonUtils.escape(config.getHost()) + "\","
                + "\"port\":" + config.getPort() + ","
                + "\"timestamp\":\"" + JsonUtils.escape(Instant.now().toString()) + "\""
                + "}";

        JsonUtils.sendJson(exchange, 200, json);
    }
}

