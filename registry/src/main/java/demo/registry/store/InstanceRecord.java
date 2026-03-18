package demo.registry.store;

public class InstanceRecord {
    private final String serviceName;
    private final String instanceId;
    private final String host;
    private final int port;
    private volatile long lastHeartbeatMs;

    public InstanceRecord(String serviceName, String instanceId, String host, int port, long lastHeartbeatMs) {
        this.serviceName = serviceName;
        this.instanceId = instanceId;
        this.host = host;
        this.port = port;
        this.lastHeartbeatMs = lastHeartbeatMs;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public long getLastHeartbeatMs() {
        return lastHeartbeatMs;
    }

    public void touch(long timestampMs) {
        this.lastHeartbeatMs = timestampMs;
    }
}

