package main.model.entity;

import main.model.TaskStatus;

public class SubtaskEntity extends BaseTaskEntity {

    public SubtaskEntity(int id, String name, String description, TaskStatus status) {
        super(id, name, description, status);
    }
}
