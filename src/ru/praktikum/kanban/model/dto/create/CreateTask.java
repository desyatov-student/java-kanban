package ru.praktikum.kanban.model.dto.create;

import java.util.Objects;
import ru.praktikum.kanban.model.TaskStatus;

public abstract class CreateTask {
    private String name;
    private String description;
    private TaskStatus status;

    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public TaskStatus getStatus() {
        return status;
    }

    public CreateTask(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = TaskStatus.NEW;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateTask that = (CreateTask) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(description, that.description) &&
                status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, status);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }
}
