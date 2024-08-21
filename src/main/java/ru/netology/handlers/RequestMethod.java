package ru.netology.handlers;

import java.util.Arrays;

public enum RequestMethod {
    GET,
    PUT,
    POST,
    PATCH,
    DELETE;

    public static boolean isMethod(String stringMethod) {
        return Arrays.stream(RequestMethod.values())
                .map(Enum::toString)
                .anyMatch(
                        enumValue -> enumValue.equals(stringMethod)
                );
    }
}
