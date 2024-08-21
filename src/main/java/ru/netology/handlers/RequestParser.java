package ru.netology.handlers;

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
}
