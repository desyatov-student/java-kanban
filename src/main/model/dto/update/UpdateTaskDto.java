package main.model.dto.update;

import java.util.Objects;
import main.model.TaskStatus;
import main.model.dto.response.EpicDto;

public class UpdateTaskDto extends BaseUpdateTask {

    private TaskStatus status;

    public TaskStatus getStatus() {
        return status;
    }

    public UpdateTaskDto(int id, String name, String description, TaskStatus status) {
        super(id, name, description);
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        UpdateTaskDto that = (UpdateTaskDto) o;
        return status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), status);
    }

    @Override
    public String toString() {
        String string = super.toString().replace("}", "");
        return string +
                ", status=" + status +
                '}';
    }
}