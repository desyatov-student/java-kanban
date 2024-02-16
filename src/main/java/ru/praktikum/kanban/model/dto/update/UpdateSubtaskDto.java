package ru.praktikum.kanban.model.dto.update;

import java.util.Objects;
import ru.praktikum.kanban.model.TaskStatus;

public class UpdateSubtaskDto extends BaseUpdateTask {

    private TaskStatus status;

    public TaskStatus getStatus() {
        return status;
    }

    public UpdateSubtaskDto(int id, String name, String description, TaskStatus status) {
        super(id, name, description);
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        UpdateSubtaskDto that = (UpdateSubtaskDto) o;
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
