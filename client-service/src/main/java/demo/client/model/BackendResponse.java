package demo.client.model;

import demo.client.util.JsonUtils;

import java.util.Map;

public class BackendResponse {
    private final String instanceId;
    private final String host;
    private final String port;
    private final String timestamp;

    private BackendResponse(String instanceId, String host, String port, String timestamp) {
        this.instanceId = instanceId;
        this.host = host;
        this.port = port;
        this.timestamp = timestamp;
    }

    public static BackendResponse fromMap(Map<String, String> map) {
        return new BackendResponse(
                map.get("instanceId"),
                map.get("host"),
                map.get("port"),
                map.get("timestamp")
        );
    }

    public String toJson() {
        return "{"
                + "\"instanceId\":\"" + JsonUtils.escape(instanceId) + "\","
                + "\"host\":\"" + JsonUtils.escape(host) + "\","
                + "\"port\":" + JsonUtils.jsonNumberOrString(port) + ","
                + "\"timestamp\":\"" + JsonUtils.escape(timestamp) + "\""
                + "}";
    }
}

