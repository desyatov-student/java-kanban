package ru.praktikum.kanban.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import ru.praktikum.kanban.model.TaskStatus;
import ru.praktikum.kanban.dto.CreateTaskDto;
import ru.praktikum.kanban.dto.TaskDto;
import ru.praktikum.kanban.dto.UpdateTaskDto;
import ru.praktikum.kanban.model.Task;
import ru.praktikum.kanban.util.StringUtils;

import static ru.praktikum.kanban.constant.DelimiterConstants.DELIMITER_COMMA;


@Mapper(config = ErrorUnmappedMapperConfig.class)
public interface TaskMapper {

    TaskDto toDto(Task task);

    void updateEntityFromDto(UpdateTaskDto dto, @MappingTarget Task task);

    default String toString(Task task) {
        return StringUtils.joining(DELIMITER_COMMA,
                task.getId(),
                TaskType.TASK,
                task.getName(),
                task.getDescription(),
                task.getStatus(),
                ""
        );
    }

    Task toEntity(int id, CreateTaskDto dto);

    default Task toEntity(String[] values) {
        return new Task(
                Integer.parseInt(values[0]),
                values[2],
                values[3],
                TaskStatus.valueOf(values[4])
        );
    }
}
