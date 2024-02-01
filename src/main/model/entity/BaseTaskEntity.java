package main.model.entity;

import java.util.Objects;
import main.model.TaskStatus;

public abstract class BaseTaskEntity {
    private int id;
    public String name;
    public String description;
    public TaskStatus status;

    public TaskStatus getStatus() {
        return status;
    }

    public int getId() {
        return id;
    }

    public BaseTaskEntity(int id, String name, String description, TaskStatus status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseTaskEntity baseTask = (BaseTaskEntity) o;
        return id == baseTask.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }
}
