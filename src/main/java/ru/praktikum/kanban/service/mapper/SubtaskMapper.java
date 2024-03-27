package ru.praktikum.kanban.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.praktikum.kanban.dto.CreateSubtaskDto;
import ru.praktikum.kanban.dto.SubtaskDto;
import ru.praktikum.kanban.dto.UpdateSubtaskDto;
import ru.praktikum.kanban.model.Subtask;
import ru.praktikum.kanban.model.TaskStatus;
import ru.praktikum.kanban.util.StringUtils;
import ru.praktikum.kanban.util.TimeUtils;

import static ru.praktikum.kanban.constant.DelimiterConstants.DELIMITER_COMMA;

@Mapper(config = ErrorUnmappedMapperConfig.class)
public interface SubtaskMapper {

    SubtaskDto toDto(Subtask subtask);

    @Mapping(target = "epicId", ignore = true)
    void updateEntityFromDto(UpdateSubtaskDto dto, @MappingTarget Subtask subtask);

    default String toString(Subtask subtask) {
        return StringUtils.joining(DELIMITER_COMMA,
                subtask.getId(),
                TaskType.SUBTASK,
                subtask.getName(),
                subtask.getDescription(),
                subtask.getStatus(),
                subtask.getEpicId(),
                subtask.getStartTime() == null ? "" : subtask.getStartTime(),
                subtask.getDuration() == null ? "" : subtask.getDuration().toMinutes()
        );
    }

    Subtask toEntity(Integer id, CreateSubtaskDto dto);

    default Subtask toEntity(String[] values) {
        return new Subtask(
                Integer.parseInt(values[0]),
                values[2],
                values[3],
                TaskStatus.valueOf(values[4]),
                Integer.parseInt(values[5]),
                TimeUtils.parseDateTime(values[6]).orElse(null),
                TimeUtils.parseDuration(values[7]).orElse(null)
        );
    }
}
