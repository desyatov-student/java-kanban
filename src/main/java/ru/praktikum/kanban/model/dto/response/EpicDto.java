package ru.praktikum.kanban.model.dto.response;

import java.util.List;
import java.util.Objects;
import lombok.Getter;
import lombok.ToString;
import ru.praktikum.kanban.model.TaskStatus;

@Getter
@ToString(callSuper = true)
public class EpicDto extends BaseTaskDto {

    private final List<SubtaskDto> subtasks;

    public EpicDto(int id, String name, String description, TaskStatus status, List<SubtaskDto> subtasks) {
        super(id, name, description, status);
        this.subtasks = subtasks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        EpicDto that = (EpicDto) o;
        return Objects.equals(subtasks, that.subtasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtasks);
    }
}
