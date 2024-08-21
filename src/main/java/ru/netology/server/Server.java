package ru.netology.server;

import ru.netology.handlers.Handler;
import ru.netology.handlers.Request;
import ru.netology.handlers.RequestMethod;
import ru.netology.handlers.RequestParser;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private static volatile Server server;
    private final ExecutorService service = Executors.newFixedThreadPool(64);
    private final ConcurrentMap<RequestMethod, ConcurrentMap<String, Handler>> handlers = new ConcurrentHashMap<>();

    private Server() {
    }

    public static Server getInstance() {
        Server localInstance = server;
        if (localInstance == null) {
            synchronized (Server.class) {
                localInstance = server;
                if (localInstance == null) {
                    server = localInstance = new Server();
                }
            }
        }
        return localInstance;
    }

    public void listen(int port) {
        try (final var serverSocket = new ServerSocket(port)) {
            while (!serverSocket.isClosed()) {
                service.submit(() -> {
                    try {
                        requestHandler(serverSocket);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }

    private void requestHandler(ServerSocket serverSocket) throws IOException {
        try (
                final var socket = serverSocket.accept();
                final var in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                final var out = new BufferedOutputStream(socket.getOutputStream())
        ) {
            final var requestLine = in.readLine();
            Optional<Request> optionalRequest = RequestParser.parse(requestLine);
            if (optionalRequest.isPresent()) {
                Request request = optionalRequest.get();
                ConcurrentMap<String, Handler> handlersMap = handlers.get(request.getMethod());
                Handler handler = handlersMap.get(request.getUrl());
                handler.handle(request, out);
            } else {
                sendBadRequestStatus(out);
            }
        }
    }

    public void addHandler(String requestMethod, String url, Handler handler) {
        if (RequestMethod.isMethod(requestMethod)) {
            if (handlers.containsKey(RequestMethod.valueOf(requestMethod))) {
                handlers.get(RequestMethod.valueOf(requestMethod)).put(url, handler);
            } else {
                ConcurrentMap<String, Handler> handlersMap = new ConcurrentHashMap<>();
                handlersMap.put(url, handler);
                handlers.put(RequestMethod.valueOf(requestMethod), handlersMap);
            }
        }
    }

    private void sendBadRequestStatus(BufferedOutputStream out) throws IOException {
        out.write((
                "HTTP/1.1 404 Not Found\r\n" +
                        "Content-Length: 0\r\n" +
                        "Connection: close\r\n" +
                        "\r\n"
        ).getBytes());
        out.flush();
    }
}
