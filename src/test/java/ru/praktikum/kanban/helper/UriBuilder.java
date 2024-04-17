package ru.praktikum.kanban.helper;

import java.net.URI;

public class UriBuilder {

    public static String LOCAL_HOST = "http://localhost:8080";

    private final String host;

    public UriBuilder() {
        this(LOCAL_HOST);
    }

    public UriBuilder(String host) {
        this.host = host;
    }

    public URI create(String path) {
        return URI.create(host + path);
    }
}
