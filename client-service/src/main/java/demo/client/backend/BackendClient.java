package demo.client.backend;

import demo.client.model.BackendResponse;
import demo.client.model.Instance;
import demo.client.util.JsonUtils;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

public class BackendClient {
    private final HttpClient httpClient = HttpClient.newHttpClient();

    public BackendResponse callBackend(Instance instance) throws Exception {
        String url = "http://" + instance.getHost() + ":" + instance.getPort() + "/api/data";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        Map<String, String> payload = JsonUtils.parseJsonObject(response.body());
        return BackendResponse.fromMap(payload);
    }
}

