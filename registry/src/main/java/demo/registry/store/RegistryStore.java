package demo.registry.store;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RegistryStore {
    private final Map<String, Map<String, InstanceRecord>> registry = new ConcurrentHashMap<>();
    private final long ttlMs;

    public RegistryStore(long ttlMs) {
        this.ttlMs = ttlMs;
    }

    public void register(String serviceName, String instanceId, String host, int port) {
        registry.computeIfAbsent(serviceName, key -> new ConcurrentHashMap<>())
                .put(instanceId, new InstanceRecord(serviceName, instanceId, host, port, System.currentTimeMillis()));
    }

    public boolean renew(String serviceName, String instanceId) {
        Map<String, InstanceRecord> instances = registry.get(serviceName);
        if (instances == null || !instances.containsKey(instanceId)) {
            return false;
        }
        instances.get(instanceId).touch(System.currentTimeMillis());
        return true;
    }

    public List<InstanceRecord> healthyInstances(String serviceName) {
        Map<String, InstanceRecord> instances = registry.getOrDefault(serviceName, Collections.emptyMap());
        long now = System.currentTimeMillis();
        List<InstanceRecord> healthy = new ArrayList<>();
        for (InstanceRecord record : instances.values()) {
            if (now - record.getLastHeartbeatMs() <= ttlMs) {
                healthy.add(record);
            }
        }
        return healthy;
    }

    public Map<String, List<InstanceRecord>> allHealthy() {
        Map<String, List<InstanceRecord>> result = new ConcurrentHashMap<>();
        for (String serviceName : registry.keySet()) {
            result.put(serviceName, healthyInstances(serviceName));
        }
        return result;
    }

    public void cleanupExpired() {
        long now = System.currentTimeMillis();
        for (Map.Entry<String, Map<String, InstanceRecord>> entry : registry.entrySet()) {
            entry.getValue().values().removeIf(record -> now - record.getLastHeartbeatMs() > ttlMs);
        }
    }
}

