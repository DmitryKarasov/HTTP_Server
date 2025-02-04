package ru.netology.handlers;

import java.io.BufferedOutputStream;

@FunctionalInterface
public interface Handler {

    void handle(Request request, BufferedOutputStream responseStream);
}
