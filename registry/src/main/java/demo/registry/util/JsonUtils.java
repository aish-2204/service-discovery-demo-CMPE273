package demo.registry.util;

import demo.registry.store.InstanceRecord;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class JsonUtils {
    private JsonUtils() {
    }

    public static Map<String, String> parseJsonObject(String body) {
        if (body == null) {
            return Collections.emptyMap();
        }
        String trimmed = body.trim();
        if (trimmed.startsWith("{")) {
            trimmed = trimmed.substring(1);
        }
        if (trimmed.endsWith("}")) {
            trimmed = trimmed.substring(0, trimmed.length() - 1);
        }
        Map<String, String> result = new HashMap<>();
        if (trimmed.isEmpty()) {
            return result;
        }
        String[] pairs = trimmed.split(",");
        for (String pair : pairs) {
            String[] kv = pair.split(":", 2);
            if (kv.length != 2) {
                continue;
            }
            String key = stripQuotes(kv[0].trim());
            String value = stripQuotes(kv[1].trim());
            result.put(key, value);
        }
        return result;
    }

    public static String buildServiceJson(List<InstanceRecord> instances) {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for (int i = 0; i < instances.size(); i++) {
            InstanceRecord record = instances.get(i);
            builder.append("{")
                    .append("\"serviceName\":\"").append(escape(record.getServiceName())).append("\",")
                    .append("\"instanceId\":\"").append(escape(record.getInstanceId())).append("\",")
                    .append("\"host\":\"").append(escape(record.getHost())).append("\",")
                    .append("\"port\":").append(record.getPort())
                    .append("}");
            if (i < instances.size() - 1) {
                builder.append(",");
            }
        }
        builder.append("]");
        return builder.toString();
    }

    public static String buildAllServicesJson(Map<String, List<InstanceRecord>> allHealthy) {
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        int index = 0;
        for (Map.Entry<String, List<InstanceRecord>> entry : allHealthy.entrySet()) {
            builder.append("\"").append(escape(entry.getKey())).append("\":");
            builder.append(buildServiceJson(entry.getValue()));
            if (index < allHealthy.size() - 1) {
                builder.append(",");
            }
            index++;
        }
        builder.append("}");
        return builder.toString();
    }

    public static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    public static String escape(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private static String stripQuotes(String value) {
        String trimmed = value.trim();
        if (trimmed.startsWith("\"")) {
            trimmed = trimmed.substring(1);
        }
        if (trimmed.endsWith("\"")) {
            trimmed = trimmed.substring(0, trimmed.length() - 1);
        }
        return trimmed;
    }
}

