package ru.praktikum.kanban.model.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import ru.praktikum.kanban.model.TaskStatus;
import ru.praktikum.kanban.model.dto.response.SubtaskDto;
import ru.praktikum.kanban.model.dto.response.TaskDto;
import ru.praktikum.kanban.util.MappingUtils;

public class EpicEntity extends TaskEntity {

    public List<Integer> subtasks = new ArrayList<>();

    public EpicEntity(int id, String name, String description, TaskStatus status) {
        super(id, name, description, status);
    }

    @Override
    public TaskDto toTaskDto(Function<EpicEntity, List<SubtaskDto>> getSubtasks) {
        List<SubtaskDto> subtasks = getSubtasks.apply(this);
        return MappingUtils.mapToEpicDto(this, subtasks);
    }
}
