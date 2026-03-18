package demo.registry.cleanup;

import demo.registry.store.RegistryStore;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CleanupScheduler {
    private final RegistryStore store;
    private final long cleanupIntervalMs;

    public CleanupScheduler(RegistryStore store, long cleanupIntervalMs) {
        this.store = store;
        this.cleanupIntervalMs = cleanupIntervalMs;
    }

    public void start() {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(store::cleanupExpired, cleanupIntervalMs, cleanupIntervalMs, TimeUnit.MILLISECONDS);
    }
}

