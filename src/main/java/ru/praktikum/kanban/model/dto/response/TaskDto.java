package ru.praktikum.kanban.model.dto.response;

import lombok.ToString;
import ru.praktikum.kanban.model.TaskStatus;

@ToString(callSuper = true)
public class TaskDto extends BaseTaskDto {

    public TaskDto(int id, String name, String description, TaskStatus status) {
        super(id, name, description, status);
    }
}
