package main.model.dto.response;

import java.util.List;
import java.util.Objects;
import main.model.TaskStatus;

public class EpicDto extends BaseTaskDto {

    private List<SubtaskDto> subtasks;

    public List<SubtaskDto> getSubtasks() {
        return subtasks;
    }

    public EpicDto(int id, String name, String description, TaskStatus status, List<SubtaskDto> subtasks) {
        super(id, name, description, status);
        this.subtasks = subtasks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        EpicDto epicDto = (EpicDto) o;
        return Objects.equals(getSubtasks(), epicDto.getSubtasks());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getSubtasks());
    }

    @Override
    public String toString() {
        String string = super.toString().replace("}", "");
        return string +
                ", subtasks=" + subtasks +
                '}';
    }
}
