package ru.netology.requests;

import ru.netology.utils.RequestParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Request {
    private final RequestMethod method;
    private final String url;
    private final Map<String, String> headers;
    private final String body;
    private final Map<String, List<String>> queryParams;

    public Request(RequestMethod method, String url, String headers, String body) {
        this.method = method;
        this.url = url.contains("?") ? url.substring(0, url.indexOf("?")) : url;
        this.headers = RequestParser.parseHeaders(headers);
        this.body = body;
        this.queryParams = RequestParser.parseParams(url);
    }

    public RequestMethod getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }

    public Map<String, List<String>> getQueryParams() {
        return queryParams;
    }

    public List<String> getQueryParam(String paramName) {
        return queryParams.get(paramName);
    }

    public Map<String, List<String>> getPostParams() {
        if (headers.containsKey("Content-Type")
                && headers.get("Content-Type").contains("application/x-www-form-urlencoded")) {
            return RequestParser.parsePostParams(body);
        }
        return new HashMap<>();
    }

    public List<String> getPostParam(String name) {
        return RequestParser.parsePostParams(body).getOrDefault(name, new ArrayList<>());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Request request = (Request) o;
        return method == request.method
                && Objects.equals(url, request.url)
                && Objects.equals(headers, request.headers)
                && Objects.equals(body, request.body)
                && Objects.equals(queryParams, request.queryParams);
    }

    @Override
    public int hashCode() {
        return Objects.hash(method, url, headers, body, queryParams);
    }

    @Override
    public String toString() {
        return "Request{" +
                "method=" + method +
                ", url='" + url + '\'' +
                ", header='" + headers + '\'' +
                ", body='" + body + '\'' +
                ", queryParams=" + queryParams +
                '}';
    }
}
