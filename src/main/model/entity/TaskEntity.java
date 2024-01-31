package main.model.entity;

import main.model.TaskStatus;

public class TaskEntity extends BaseTaskEntity {

    public TaskEntity(int id, String name, String description, TaskStatus status) {
        super(id, name, description, status);
    }
}
