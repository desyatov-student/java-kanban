package ru.praktikum.kanban.model.entity;

import ru.praktikum.kanban.model.TaskStatus;

public class SubtaskEntity extends TaskEntity {

    int epicId;

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    public SubtaskEntity(int id, String name, String description, TaskStatus status) {
        super(id, name, description, status);
    }
}
