package ru.praktikum.kanban.model.entity;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import ru.praktikum.kanban.model.TaskStatus;
import ru.praktikum.kanban.model.dto.response.SubtaskDto;
import ru.praktikum.kanban.model.dto.response.TaskDto;

public abstract class TaskEntity {
    private final int id;
    public String name;
    public String description;
    public TaskStatus status;

    public TaskStatus getStatus() {
        return status;
    }

    public int getId() {
        return id;
    }

    public TaskEntity(int id, String name, String description, TaskStatus status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public abstract TaskDto toTaskDto(Function<EpicEntity, List<SubtaskDto>> getSubtasks);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (!(o instanceof TaskEntity)) {
           return false;
        }
        TaskEntity baseTask = (TaskEntity) o;
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
