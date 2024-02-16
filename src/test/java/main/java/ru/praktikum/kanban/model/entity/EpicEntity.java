package main.java.ru.praktikum.kanban.model.entity;

import java.util.ArrayList;
import java.util.List;
import main.java.ru.praktikum.kanban.model.TaskStatus;

public class EpicEntity extends BaseTaskEntity {

    public List<Integer> subtasks = new ArrayList<>();

    public EpicEntity(int id, String name, String description, TaskStatus status) {
        super(id, name, description, status);
    }
}
