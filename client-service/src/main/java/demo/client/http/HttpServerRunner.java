package demo.client.http;

import com.sun.net.httpserver.HttpServer;
import demo.client.config.ClientConfig;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class HttpServerRunner {
    private final ClientConfig config;
    private final ClientHandler handler;

    public HttpServerRunner(ClientConfig config, ClientHandler handler) {
        this.config = config;
        this.handler = handler;
    }

    public void start() throws Exception {
        HttpServer httpServer = HttpServer.create(new InetSocketAddress(config.getPort()), 0);
        httpServer.createContext("/call-backend", handler);
        httpServer.setExecutor(Executors.newCachedThreadPool());
        httpServer.start();

        System.out.println("Client service started on http://" + config.getHost() + ":" + config.getPort());
    }
}

