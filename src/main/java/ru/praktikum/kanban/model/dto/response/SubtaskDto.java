package ru.praktikum.kanban.model.dto.response;

import ru.praktikum.kanban.model.TaskStatus;

public class SubtaskDto extends BaseTaskDto {

    public SubtaskDto(int id, String name, String description, TaskStatus status) {
        super(id, name, description, status);
    }

}