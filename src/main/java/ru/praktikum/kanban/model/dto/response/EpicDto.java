package ru.praktikum.kanban.model.dto.response;

import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import ru.praktikum.kanban.model.TaskStatus;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class EpicDto extends TaskDto {

    @NonNull private final List<SubtaskDto> subtasks;

    public EpicDto(int id, String name, String description, TaskStatus status, List<SubtaskDto> subtasks) {
        super(id, name, description, status);
        this.subtasks = subtasks;
    }
}
