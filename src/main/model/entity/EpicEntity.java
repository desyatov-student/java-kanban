package main.model.entity;

import main.model.TaskStatus;

public class EpicEntity extends BaseTaskEntity {

    public EpicEntity(int id, String name, String description, TaskStatus status) {
        super(id, name, description, status);
    }
}
