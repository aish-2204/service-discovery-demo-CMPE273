package demo.registry.util;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import demo.registry.config.RegistryConfig;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executors;

public final class HttpUtils {
    private HttpUtils() {
    }

    public static void startServer(RegistryConfig config,
                                   HttpHandler registerHandler,
                                   HttpHandler heartbeatHandler,
                                   HttpHandler servicesHandler,
                                   HttpHandler uiHandler) throws Exception {
        HttpServer httpServer = HttpServer.create(new InetSocketAddress(config.getHost(), config.getPort()), 0);
        httpServer.createContext("/register", registerHandler);
        httpServer.createContext("/heartbeat", heartbeatHandler);
        httpServer.createContext("/services", servicesHandler);
        httpServer.createContext("/ui", uiHandler);
        httpServer.setExecutor(Executors.newCachedThreadPool());
        httpServer.start();

        System.out.println("Registry started on http://" + config.getHost() + ":" + config.getPort());
        System.out.println("UI available at http://" + config.getHost() + ":" + config.getPort() + "/ui");
    }

    public static void sendResponse(HttpExchange exchange, int status, String response) throws IOException {
        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(status, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    public static void sendJson(HttpExchange exchange, int status, String json) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        sendResponse(exchange, status, json);
    }

    public static String readBody(InputStream inputStream) throws IOException {
        return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
    }
}

