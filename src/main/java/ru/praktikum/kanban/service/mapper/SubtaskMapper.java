package ru.praktikum.kanban.service.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.praktikum.kanban.dto.CreateSubtaskDto;
import ru.praktikum.kanban.dto.SubtaskDto;
import ru.praktikum.kanban.dto.UpdateSubtaskDto;
import ru.praktikum.kanban.model.Subtask;
import ru.praktikum.kanban.model.TaskStatus;
import ru.praktikum.kanban.util.StringUtils;
import ru.praktikum.kanban.util.TimeUtils;

import static ru.praktikum.kanban.constant.CsvConstants.INDEX_TASK_DESCRIPTION;
import static ru.praktikum.kanban.constant.CsvConstants.INDEX_TASK_DURATION;
import static ru.praktikum.kanban.constant.CsvConstants.INDEX_TASK_EPIC_ID;
import static ru.praktikum.kanban.constant.CsvConstants.INDEX_TASK_ID;
import static ru.praktikum.kanban.constant.CsvConstants.INDEX_TASK_NAME;
import static ru.praktikum.kanban.constant.CsvConstants.INDEX_TASK_START_TIME;
import static ru.praktikum.kanban.constant.CsvConstants.INDEX_TASK_STATUS;
import static ru.praktikum.kanban.constant.DelimiterConstants.DELIMITER_COMMA;

@Mapper(config = ErrorUnmappedMapperConfig.class)
public interface SubtaskMapper {

    SubtaskDto toDto(Subtask subtask);

    @Mapping(target = "epicId", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
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

    @Mapping(target = "status", expression = "java(TaskStatus.NEW)")
    Subtask toEntity(Integer id, CreateSubtaskDto dto);

    default Subtask toEntity(String[] values) {
        return new Subtask(
                Integer.parseInt(values[INDEX_TASK_ID]),
                values[INDEX_TASK_NAME],
                values[INDEX_TASK_DESCRIPTION],
                TaskStatus.valueOf(values[INDEX_TASK_STATUS]),
                Integer.parseInt(values[INDEX_TASK_EPIC_ID]),
                TimeUtils.parseDateTime(values[INDEX_TASK_START_TIME]).orElse(null),
                TimeUtils.parseDuration(values[INDEX_TASK_DURATION]).orElse(null)
        );
    }
}
