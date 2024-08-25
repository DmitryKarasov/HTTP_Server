package ru.netology.utils;

import ru.netology.requests.Request;
import ru.netology.requests.RequestMethod;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class RequestParser {
    public static Optional<Request> parse(String requestLine) {
        var parts = requestLine.split("\n\n");
        var body = parts.length > 1 ? parts[1] : "";
        var startLine = parts[0].split(" ");

        return RequestMethod.isMethod(startLine[0])
                ? Optional.of(new Request(RequestMethod.valueOf(startLine[0]), startLine[1], startLine[2], body))
                : Optional.empty();
    }

    public static Map<String, List<String>> parseParams(String query) {
        Map<String, List<String>> result = new HashMap<>();
        if (query.contains("?")) {
            String[] pairs = query.substring(query.indexOf("?") + 1).split("&");
            for (String pair : pairs) {
                String[] parts = pair.split("=");
                String key = URLDecoder.decode(parts[0], StandardCharsets.UTF_8);
                String value = parts.length > 1 ? URLDecoder.decode(parts[1], StandardCharsets.UTF_8) : null;
                if (result.containsKey(key)) {
                    result.get(key).add(value);
                } else {
                    List<String> values = new ArrayList<>();
                    values.add(value);
                    result.put(key, values);
                }
            }
        }
        return result;
    }
}
