package ru.netology.handlers;

import java.util.Objects;

public class Request {

    private final RequestMethod method;
    private final String url;
    private final String header;
    private final String body;

    public Request(RequestMethod method, String url, String header, String body) {
        this.method = method;
        this.url = url;
        this.header = header;
        this.body = body;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Request request = (Request) o;
        return method == request.method
                && Objects.equals(url, request.url)
                && Objects.equals(header, request.header)
                && Objects.equals(body, request.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(method, url, header, body);
    }

    @Override
    public String toString() {
        return "Request{" +
                "method=" + method +
                ", url='" + url + '\'' +
                ", header='" + header + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}
