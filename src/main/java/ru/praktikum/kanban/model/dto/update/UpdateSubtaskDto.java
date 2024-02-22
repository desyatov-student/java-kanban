package ru.praktikum.kanban.model.dto.update;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import ru.praktikum.kanban.model.TaskStatus;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class UpdateSubtaskDto extends BaseUpdateTask {

    private final TaskStatus status;

    public UpdateSubtaskDto(int id, String name, String description, TaskStatus status) {
        super(id, name, description);
        this.status = status;
    }

}
