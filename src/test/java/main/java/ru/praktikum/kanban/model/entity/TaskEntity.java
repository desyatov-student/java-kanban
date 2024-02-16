package main.java.ru.praktikum.kanban.model.entity;

import main.java.ru.praktikum.kanban.model.TaskStatus;

public class TaskEntity extends BaseTaskEntity {

    public TaskEntity(int id, String name, String description, TaskStatus status) {
        super(id, name, description, status);
    }
}
