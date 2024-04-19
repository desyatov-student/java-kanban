package ru.praktikum.kanban.helper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.net.http.HttpResponse;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Response {

    private Gson gson;
    private HttpResponse<String> response;

    public int statusCode() {
        return response.statusCode();
    }

    public String body() {
        return response.body();
    }

    public <T> T decodeBody(Class<T> tClass) {
        return gson.fromJson(response.body(), tClass);
    }

    public <T> T decodeBody(TypeToken<T> typeOfT) {
        return gson.fromJson(response.body(), typeOfT);
    }
}
