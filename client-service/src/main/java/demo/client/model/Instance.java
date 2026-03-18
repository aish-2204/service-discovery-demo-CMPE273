package demo.client.model;

import java.util.HashMap;
import java.util.Map;

public class Instance {
    private final String instanceId;
    private final String host;
    private final int port;

    public Instance(String instanceId, String host, int port) {
        this.instanceId = instanceId;
        this.host = host;
        this.port = port;
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

    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        map.put("instanceId", instanceId);
        map.put("host", host);
        map.put("port", String.valueOf(port));
        return map;
    }
}

