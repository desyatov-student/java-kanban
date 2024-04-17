package ru.praktikum.kanban.service.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.time.Duration;
import java.time.LocalDateTime;
import ru.praktikum.kanban.type.adapter.DurationAdapter;
import ru.praktikum.kanban.type.adapter.LocalDateTimeAdapter;

public final class GsonFactory {
    private GsonFactory() {
    }

    public static Gson taskGson() {
        return new GsonBuilder()
                .serializeNulls()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .create();
    }
}
