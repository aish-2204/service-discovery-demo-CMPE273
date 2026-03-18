package demo.client;

import demo.client.config.ClientConfig;
import demo.client.http.ClientHandler;
import demo.client.http.HttpServerRunner;

public class ClientService {
    public static void main(String[] args) throws Exception {
        ClientConfig config = ClientConfig.fromEnv();
        ClientHandler handler = new ClientHandler(config);
        HttpServerRunner serverRunner = new HttpServerRunner(config, handler);
        serverRunner.start();
    }
}

