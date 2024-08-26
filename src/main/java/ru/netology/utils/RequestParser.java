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
        var startLine = parts[0].split("\n", 2);
        var headers = startLine.length > 1 ? startLine[1] : "";
        var method = parts[0].split(" ")[0];
        var url = parts[0].split(" ")[1];

        return RequestMethod.isMethod(method)
                ? Optional.of(new Request(RequestMethod.valueOf(method), url, headers, body))
                : Optional.empty();
    }

    public static Map<String, List<String>> parseParams(String query) {
        Map<String, List<String>> result = new HashMap<>();
        if (query.contains("?")) {
            String[] pairs = query.substring(query.indexOf("?") + 1).split("&");
            splitPairs(pairs, result);
        }
        return result;
    }

    public static Map<String, List<String>> parsePostParams(String body) {
        Map<String, List<String>> result = new HashMap<>();
        String[] pairs = body.split("&");
        splitPairs(pairs, result);
        return result;
    }

    public static Map<String, String> parseHeaders(String headers) {
        Map<String, String> result = new HashMap<>();
        String[] pairs = headers.split("\n");
        for (String pair : pairs) {
            result.put(pair.split(": ")[0], pair.split(": ")[1]);
        }
        return result;
    }

    private static void splitPairs(String[] pairs, Map<String, List<String>> result) {
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

}
