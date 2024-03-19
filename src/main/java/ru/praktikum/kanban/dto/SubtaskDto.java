package ru.praktikum.kanban.dto;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import ru.praktikum.kanban.model.TaskStatus;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SubtaskDto extends TaskDto {

    public SubtaskDto(Integer id, String name, String description, TaskStatus status) {
        super(id, name, description, status);
    }

}
