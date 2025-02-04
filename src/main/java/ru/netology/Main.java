package ru.netology;

import ru.netology.server.Server;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;


public class Main {
    public static void main(String[] args) {

        Server server = Server.getInstance();

        server.addHandler("GET", "/classic.html", (request, responseStream) -> {
            try {
                final var filePath = Path.of(".", "public", request.getUrl());
                final var mimeType = Files.probeContentType(filePath);

                final var template = Files.readString(filePath);
                final var content = template.replace(
                        "{time}",
                        LocalDateTime.now().toString()
                ).getBytes();
                responseStream.write((
                        "HTTP/1.1 200 OK\r\n" +
                                "Content-Type: " + mimeType + "\r\n" +
                                "Content-Length: " + content.length + "\r\n" +
                                "Connection: close\r\n" +
                                "\r\n"
                ).getBytes());

                responseStream.write(content);
                responseStream.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        server.addHandler("GET", "/spring.png", (request, responseStream) -> {
            try {
                final var filePath = Path.of(".", "public", request.getUrl());
                final var mimeType = Files.probeContentType(filePath);

                final var length = Files.size(filePath);
                responseStream.write((
                        "HTTP/1.1 200 OK\r\n" +
                                "Content-Type: " + mimeType + "\r\n" +
                                "Content-Length: " + length + "\r\n" +
                                "Connection: close\r\n" +
                                "\r\n"
                ).getBytes());
                Files.copy(filePath, responseStream);
                responseStream.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        server.listen(9999);

    }
}


