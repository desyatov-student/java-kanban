package ru.praktikum.kanban.model.entity;

import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.praktikum.kanban.model.TaskStatus;

@Getter
@AllArgsConstructor
@ToString
public abstract class BaseTaskEntity {
    private final int id;
    @Setter
    public String name;
    @Setter
    public String description;
    @Setter
    public TaskStatus status;

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
}
