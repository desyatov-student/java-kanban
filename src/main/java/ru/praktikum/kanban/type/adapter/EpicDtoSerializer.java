package ru.praktikum.kanban.type.adapter;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import ru.praktikum.kanban.dto.EpicDto;
import ru.praktikum.kanban.service.http.GsonFactory;
import ru.praktikum.kanban.service.mapper.TaskType;

public class EpicDtoSerializer implements JsonSerializer<EpicDto> {

    @Override
    public JsonElement serialize(EpicDto src, Type typeOfSrc,
                                 JsonSerializationContext context) {
        Gson gson = GsonFactory.taskGson();
        JsonElement jsonElement = gson.toJsonTree(src);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        jsonObject.addProperty("type", TaskType.EPIC.toString());
        return jsonObject;
    }
}
