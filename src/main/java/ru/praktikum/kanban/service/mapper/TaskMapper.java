package ru.praktikum.kanban.service.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.praktikum.kanban.dto.CreateTaskDto;
import ru.praktikum.kanban.dto.TaskDto;
import ru.praktikum.kanban.dto.UpdateTaskDto;
import ru.praktikum.kanban.model.Task;
import ru.praktikum.kanban.model.TaskStatus;
import ru.praktikum.kanban.util.StringUtils;
import ru.praktikum.kanban.util.TimeUtils;

import static ru.praktikum.kanban.constant.CsvConstants.INDEX_TASK_DESCRIPTION;
import static ru.praktikum.kanban.constant.CsvConstants.INDEX_TASK_DURATION;
import static ru.praktikum.kanban.constant.CsvConstants.INDEX_TASK_ID;
import static ru.praktikum.kanban.constant.CsvConstants.INDEX_TASK_NAME;
import static ru.praktikum.kanban.constant.CsvConstants.INDEX_TASK_START_TIME;
import static ru.praktikum.kanban.constant.CsvConstants.INDEX_TASK_STATUS;
import static ru.praktikum.kanban.constant.DelimiterConstants.DELIMITER_COMMA;


@Mapper(config = ErrorUnmappedMapperConfig.class)
public interface TaskMapper {

    TaskDto toDto(Task task);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(UpdateTaskDto dto, @MappingTarget Task task);

    default String toString(Task task) {
        return StringUtils.joining(DELIMITER_COMMA,
                task.getId(),
                TaskType.TASK,
                task.getName(),
                task.getDescription(),
                task.getStatus(),
                "",
                task.getStartTime() == null ? "" : task.getStartTime(),
                task.getDuration() == null ? "" : task.getDuration().toMinutes()
        );
    }

    @Mapping(target = "status", expression = "java(TaskStatus.NEW)")
    Task toEntity(Integer id, CreateTaskDto dto);

    default Task toEntity(String[] values) {
        return new Task(
                Integer.parseInt(values[INDEX_TASK_ID]),
                values[INDEX_TASK_NAME],
                values[INDEX_TASK_DESCRIPTION],
                TaskStatus.valueOf(values[INDEX_TASK_STATUS]),
                TimeUtils.parseDateTime(values[INDEX_TASK_START_TIME]).orElse(null),
                TimeUtils.parseDuration(values[INDEX_TASK_DURATION]).orElse(null)
        );
    }
}
