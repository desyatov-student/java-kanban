package ru.praktikum.kanban.model.dto.create;

import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import ru.praktikum.kanban.model.TaskStatus;

@Getter
@AllArgsConstructor
@ToString
public abstract class BaseCreateTask {
    private final String name;
    private final String description;
    private final TaskStatus status = TaskStatus.NEW;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BaseCreateTask that = (BaseCreateTask) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(description, that.description) &&
                status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, status);
    }

}
