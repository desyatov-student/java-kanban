package ru.praktikum.kanban.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import ru.praktikum.kanban.model.TaskStatus;
import ru.praktikum.kanban.model.TaskType;
import ru.praktikum.kanban.model.dto.create.CreateTaskDto;
import ru.praktikum.kanban.model.dto.response.TaskDto;
import ru.praktikum.kanban.model.dto.update.UpdateTaskDto;
import ru.praktikum.kanban.model.entity.Task;
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
                task.name,
                task.description,
                task.status,
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
