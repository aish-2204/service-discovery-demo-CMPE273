package demo.client.registry;

import demo.client.model.Instance;
import demo.client.util.JsonUtils;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RegistryClient {
    private final String registryUrl;
    private final HttpClient httpClient = HttpClient.newHttpClient();

    public RegistryClient(String registryUrl) {
        this.registryUrl = registryUrl;
    }

    public List<Instance> fetchInstances(String serviceName) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(registryUrl + "/services/" + serviceName))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            return List.of();
        }

        return parseInstancesArray(response.body());
    }

    private List<Instance> parseInstancesArray(String jsonArray) {
        String trimmed = JsonUtils.trimJsonArray(jsonArray);
        List<String> objects = JsonUtils.splitJsonObjects(trimmed);
        List<Instance> instances = new ArrayList<>();
        for (String object : objects) {
            Map<String, String> map = JsonUtils.parseJsonObject(object);
            String instanceId = map.get("instanceId");
            String host = map.get("host");
            String portValue = map.get("port");
            if (instanceId == null || host == null || portValue == null) {
                continue;
            }
            instances.add(new Instance(instanceId, host, Integer.parseInt(portValue)));
        }
        return instances;
    }
}

