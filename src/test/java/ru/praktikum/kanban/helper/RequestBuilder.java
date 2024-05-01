package ru.praktikum.kanban.helper;

import com.google.gson.Gson;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import ru.praktikum.kanban.service.http.GsonFactory;
import ru.praktikum.kanban.service.http.RequestMethod;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RequestBuilder {
    private final HttpClient client;
    private final Gson gson;
    private final UriBuilder uriBuilder;
    private RequestMethod method;
    private String path;
    private Object requestBody;
    private Integer statusCode;

    public RequestBuilder() {
        this.client = HttpClient.newHttpClient();
        this.gson = GsonFactory.taskGson();
        this.uriBuilder = new UriBuilder();
    }

    public RequestBuilder GET() {
        this.method = RequestMethod.GET;
        return this;
    }

    public RequestBuilder POST() {
        this.method = RequestMethod.POST;
        return this;
    }

    public RequestBuilder PATCH() {
        this.method = RequestMethod.PATCH;
        return this;
    }

    public RequestBuilder DELETE() {
        this.method = RequestMethod.DELETE;
        return this;
    }

    public RequestBuilder path(String path) {
        assert path != null;
        this.path = path;
        return this;
    }

    public RequestBuilder requestBody(Object requestBody) {
        assert requestBody != null;
        this.requestBody = requestBody;
        return this;
    }

    public RequestBuilder acceptableStatusCode(Integer statusCode) {
        assert statusCode != null;
        this.statusCode = statusCode;
        return this;
    }

    public Response send() throws IOException, InterruptedException {
        URI uri = uriBuilder.create(path);
        String bodyJson;
        if (requestBody != null) {
            bodyJson = gson.toJson(requestBody);
        } else {
            bodyJson = "";
        }
        HttpRequest request = HttpRequest.newBuilder()
                .method(method.toString(), HttpRequest.BodyPublishers.ofString(bodyJson))
                .uri(uri).build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);
        if (statusCode != null) {
            assertEquals(statusCode, response.statusCode());
        }
        return new Response(gson, response);
    }
}
