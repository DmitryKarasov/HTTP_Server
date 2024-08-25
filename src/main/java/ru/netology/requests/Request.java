package ru.netology.requests;

import ru.netology.utils.RequestParser;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Request {
    private final RequestMethod method;
    private final String url;
    private final String header;
    private final String body;
    private final Map<String, List<String>> queryParams;

    public Request(RequestMethod method, String url, String header, String body) {
        this.method = method;
        this.url = url.substring(0, url.indexOf("?"));
        this.header = header;
        this.body = body;
        this.queryParams = RequestParser.parseParams(url);
    }

    public RequestMethod getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public String getHeader() {
        return header;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Request request = (Request) o;
        return method == request.method
                && Objects.equals(url, request.url)
                && Objects.equals(header, request.header)
                && Objects.equals(body, request.body)
                && Objects.equals(queryParams, request.queryParams);
    }

    @Override
    public int hashCode() {
        return Objects.hash(method, url, header, body, queryParams);
    }
}
