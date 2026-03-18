package demo.backend;

import demo.backend.config.BackendConfig;
import demo.backend.http.DataHandler;
import demo.backend.http.HttpServerRunner;
import demo.backend.registry.RegistryClient;

public class BackendService {
    public static void main(String[] args) throws Exception {
        BackendConfig config = BackendConfig.fromEnv();
        DataHandler handler = new DataHandler(config);
        HttpServerRunner serverRunner = new HttpServerRunner(config, handler);
        RegistryClient registryClient = new RegistryClient(config);

        serverRunner.start();
        registryClient.register();
        registryClient.startHeartbeat();
    }
}

