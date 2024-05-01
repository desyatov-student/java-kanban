package ru.praktikum.kanban.service.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.time.Duration;
import java.time.LocalDateTime;
import ru.praktikum.kanban.dto.EpicDto;
import ru.praktikum.kanban.dto.SubtaskDto;
import ru.praktikum.kanban.dto.TaskDto;
import ru.praktikum.kanban.type.adapter.DurationAdapter;
import ru.praktikum.kanban.type.adapter.EpicDtoSerializer;
import ru.praktikum.kanban.type.adapter.LocalDateTimeAdapter;
import ru.praktikum.kanban.type.adapter.SubtaskDtoSerializer;
import ru.praktikum.kanban.type.adapter.TaskDtoSerializer;

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

    public static Gson gsonWithCustomSerializer() {
        return new GsonBuilder()
                .serializeNulls()
                .registerTypeAdapter(EpicDto.class, new EpicDtoSerializer())
                .registerTypeAdapter(SubtaskDto.class, new SubtaskDtoSerializer())
                .registerTypeAdapter(TaskDto.class, new TaskDtoSerializer())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .create();
    }
}
