package ru.praktikum.kanban.type.adapter;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import ru.praktikum.kanban.dto.TaskDto;
import ru.praktikum.kanban.service.http.GsonFactory;
import ru.praktikum.kanban.service.mapper.TaskType;

public class TaskDtoSerializer implements JsonSerializer<TaskDto> {

    @Override
    public JsonElement serialize(TaskDto src, Type typeOfSrc,
                                 JsonSerializationContext context) {
        Gson gson = GsonFactory.taskGson();
        JsonElement jsonElement = gson.toJsonTree(src);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        jsonObject.addProperty("type", TaskType.TASK.toString());
        return jsonObject;
    }
}

