package demo.client.model;

import demo.client.util.JsonUtils;

import java.time.Instant;

public class ClientResponse {
    private final Instance selectedInstance;
    private final BackendResponse backendResponse;
    private final String timestamp;

    private ClientResponse(Instance selectedInstance, BackendResponse backendResponse, String timestamp) {
        this.selectedInstance = selectedInstance;
        this.backendResponse = backendResponse;
        this.timestamp = timestamp;
    }

    public static ClientResponse from(Instance selectedInstance, BackendResponse backendResponse) {
        return new ClientResponse(selectedInstance, backendResponse, Instant.now().toString());
    }

    public String toJson() {
        return "{"
                + "\"selectedInstance\":" + JsonUtils.toJsonObject(selectedInstance.toMap()) + ","
                + "\"backendResponse\":" + backendResponse.toJson() + ","
                + "\"timestamp\":\"" + JsonUtils.escape(timestamp) + "\""
                + "}";
    }
}

