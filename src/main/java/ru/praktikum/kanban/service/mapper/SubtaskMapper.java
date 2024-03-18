package ru.praktikum.kanban.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.praktikum.kanban.model.TaskStatus;
import ru.praktikum.kanban.model.TaskType;
import ru.praktikum.kanban.model.CreateSubtask;
import ru.praktikum.kanban.model.SubtaskDto;
import ru.praktikum.kanban.model.UpdateSubtask;
import ru.praktikum.kanban.model.Subtask;
import ru.praktikum.kanban.util.StringUtils;

import static ru.praktikum.kanban.constant.DelimiterConstants.DELIMITER_COMMA;

@Mapper(config = ErrorUnmappedMapperConfig.class)
public interface SubtaskMapper {

    SubtaskDto toDto(Subtask subtask);

    @Mapping(target = "epicId", ignore = true)
    void updateEntityFromDto(UpdateSubtask dto, @MappingTarget Subtask subtask);

    default String toString(Subtask subtask) {
        return StringUtils.joining(DELIMITER_COMMA,
                subtask.getId(),
                TaskType.SUBTASK,
                subtask.getName(),
                subtask.getDescription(),
                subtask.getStatus(),
                subtask.getEpicId()
        );
    }

    Subtask toEntity(int id, CreateSubtask dto);

    default Subtask toEntity(String[] values) {
        return new Subtask(
                Integer.parseInt(values[0]),
                values[2],
                values[3],
                TaskStatus.valueOf(values[4]),
                Integer.parseInt(values[5])
        );
    }
}
