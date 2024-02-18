package ru.praktikum.kanban.model.entity;

import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import ru.praktikum.kanban.model.TaskStatus;

@Getter
@Setter
public abstract class BaseTaskEntity {
    public int id;
    public String name;
    public String description;
    public TaskStatus status;

    public BaseTaskEntity(int id, String name, String description, TaskStatus status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (!(o instanceof BaseTaskEntity)) {
            return false;
        }
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