package ru.praktikum.kanban.model.entity;

import java.util.List;
import java.util.function.Function;
import ru.praktikum.kanban.model.TaskStatus;
import ru.praktikum.kanban.model.dto.response.SubtaskDto;
import ru.praktikum.kanban.model.dto.response.BaseTaskDto;
import ru.praktikum.kanban.util.MappingUtils;

public class SubtaskEntityBase extends BaseTaskEntity {

    int epicId;

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    public SubtaskEntityBase(int id, String name, String description, TaskStatus status) {
        super(id, name, description, status);
    }

    @Override
    public BaseTaskDto toTaskDto(Function<EpicEntity, List<SubtaskDto>> getSubtasks) {
        return MappingUtils.mapToSubtaskDto(this);
    }
}
