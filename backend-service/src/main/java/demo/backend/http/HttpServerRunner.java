package demo.backend.http;

import com.sun.net.httpserver.HttpServer;
import demo.backend.config.BackendConfig;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class HttpServerRunner {
    private final BackendConfig config;
    private final DataHandler handler;

    public HttpServerRunner(BackendConfig config, DataHandler handler) {
        this.config = config;
        this.handler = handler;
    }

    public void start() throws Exception {
        HttpServer httpServer = HttpServer.create(new InetSocketAddress(config.getPort()), 0);
        httpServer.createContext("/api/data", handler);
        httpServer.setExecutor(Executors.newCachedThreadPool());
        httpServer.start();

        System.out.println("Backend instance started on http://" + config.getHost() + ":" + config.getPort() + " with ID " + config.getInstanceId());
    }
}

