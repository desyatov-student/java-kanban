package ru.praktikum.kanban.model.dto.update;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class UpdateEpicDto extends BaseUpdateTask {

    public UpdateEpicDto(int id, String name, String description) {
        super(id, name, description);
    }
}
