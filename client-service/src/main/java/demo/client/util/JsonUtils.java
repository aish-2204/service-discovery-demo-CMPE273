package demo.client.util;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class JsonUtils {
    private JsonUtils() {
    }

    public static void sendJson(HttpExchange exchange, int status, String json) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(status, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    public static String trimJsonArray(String jsonArray) {
        String trimmed = jsonArray == null ? "" : jsonArray.trim();
        if (trimmed.startsWith("[")) {
            trimmed = trimmed.substring(1);
        }
        if (trimmed.endsWith("]")) {
            trimmed = trimmed.substring(0, trimmed.length() - 1);
        }
        return trimmed;
    }

    public static List<String> splitJsonObjects(String body) {
        List<String> objects = new ArrayList<>();
        if (body == null || body.trim().isEmpty()) {
            return objects;
        }
        int depth = 0;
        int start = 0;
        for (int i = 0; i < body.length(); i++) {
            char ch = body.charAt(i);
            if (ch == '{') {
                if (depth == 0) {
                    start = i;
                }
                depth++;
            } else if (ch == '}') {
                depth--;
                if (depth == 0) {
                    objects.add(body.substring(start, i + 1));
                }
            }
        }
        return objects;
    }

    public static Map<String, String> parseJsonObject(String body) {
        if (body == null) {
            return Map.of();
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

    public static String toJsonObject(Map<String, String> map) {
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        int index = 0;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            builder.append("\"").append(escape(entry.getKey())).append("\":");
            String value = entry.getValue();
            builder.append(jsonNumberOrString(value));
            if (index < map.size() - 1) {
                builder.append(",");
            }
            index++;
        }
        builder.append("}");
        return builder.toString();
    }

    public static String jsonNumberOrString(String value) {
        if (value != null && value.matches("\\d+")) {
            return value;
        }
        return "\"" + escape(value) + "\"";
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

