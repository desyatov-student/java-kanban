package main.models.dto;

import java.util.Objects;
import main.models.BaseTask;
import main.models.TaskStatus;

public class SubtaskDto extends BaseTask {

    private TaskStatus status;

    public TaskStatus getStatus() {
        return status;
    }

    public SubtaskDto(int id, String name, String description, TaskStatus status) {
        super(id, name, description);
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SubtaskDto subtaskDto = (SubtaskDto) o;
        return getStatus() == subtaskDto.getStatus() &&
                getId() == subtaskDto.getId() &&
                Objects.equals(getName(), subtaskDto.getName()) &&
                Objects.equals(getDescription(), subtaskDto.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getStatus(), getId(), getName(), getDescription());
    }

    @Override
    public String toString() {
        return "SubtaskDto{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + status +
                '}';
    }
}
