package main.model.dto.response;

import java.util.Objects;
import main.model.TaskStatus;

public abstract class BaseTaskDto {
    private int id;
    private String name;
    private String description;
    private TaskStatus status;

    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public TaskStatus getStatus() {
        return status;
    }

    public BaseTaskDto(int id, String name, String description, TaskStatus status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseTaskDto that = (BaseTaskDto) o;
        return getId() == that.getId() &&
                Objects.equals(getName(), that.getName()) &&
                Objects.equals(getDescription(), that.getDescription()) &&
                getStatus() == that.getStatus();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getDescription(), getStatus());
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
