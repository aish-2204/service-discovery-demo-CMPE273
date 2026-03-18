package demo.registry;

import demo.registry.config.RegistryConfig;
import demo.registry.http.HeartbeatHandler;
import demo.registry.http.RegisterHandler;
import demo.registry.http.ServicesHandler;
import demo.registry.http.UiHandler;
import demo.registry.store.RegistryStore;
import demo.registry.util.HttpUtils;
import demo.registry.cleanup.CleanupScheduler;

public class RegistryServer {
    public static void main(String[] args) throws Exception {
        RegistryConfig config = RegistryConfig.fromEnv();
        RegistryStore store = new RegistryStore(config.getTtlMs());

        HttpUtils.startServer(
                config,
                new RegisterHandler(store),
                new HeartbeatHandler(store),
                new ServicesHandler(store),
                new UiHandler()
        );

        CleanupScheduler scheduler = new CleanupScheduler(store, config.getCleanupIntervalMs());
        scheduler.start();
    }
}

