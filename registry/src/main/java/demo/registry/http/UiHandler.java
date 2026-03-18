package demo.registry.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import demo.registry.util.HttpUtils;

import java.io.IOException;
import java.time.Instant;

public class UiHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
            HttpUtils.sendResponse(exchange, 405, "Method Not Allowed");
            return;
        }

        String html = "<!doctype html>\n"
                + "<html><head><title>Service Registry</title></head>\n"
                + "<body style='font-family: Arial, sans-serif;'>\n"
                + "<h2>Service Registry</h2>\n"
                + "<p>View all services via <code>/services</code>.</p>\n"
                + "<p>Example: <a href='/services/backend-service'>/services/backend-service</a></p>\n"
                + "<p>Last refreshed: " + Instant.now() + "</p>\n"
                + "</body></html>";

        exchange.getResponseHeaders().set("Content-Type", "text/html; charset=utf-8");
        HttpUtils.sendResponse(exchange, 200, html);
    }
}

