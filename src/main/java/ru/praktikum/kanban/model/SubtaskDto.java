package ru.praktikum.kanban.model;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SubtaskDto extends TaskDto {

    public SubtaskDto(int id, String name, String description, TaskStatus status) {
        super(id, name, description, status);
    }

}
