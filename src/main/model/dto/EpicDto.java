package main.model.dto;

import java.util.List;
import java.util.Objects;
import main.model.BaseTask;
import main.model.TaskStatus;

public class EpicDto extends BaseTask {

    private List<SubtaskDto> subtasks;
    private TaskStatus status;

    public List<SubtaskDto> getSubtasks() {
        return subtasks;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public EpicDto(int id, String name, String description, TaskStatus status, List<SubtaskDto> subtasks) {
        super(id, name, description);
        this.subtasks = subtasks;
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        EpicDto epicDto = (EpicDto) o;
        return Objects.equals(getSubtasks(), epicDto.getSubtasks()) &&
                getStatus() == epicDto.getStatus() &&
                getId() == epicDto.getId() &&
                Objects.equals(getName(), epicDto.getName()) &&
                Objects.equals(getDescription(), epicDto.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getSubtasks(), getStatus(), getId(), getName(), getDescription());
    }

    @Override
    public String toString() {
        return "EpicDto{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + status +
                ", subtasks=" + subtasks +
                '}';
    }
}
