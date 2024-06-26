package ru.praktikum.kanban.service.mapper;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import ru.praktikum.kanban.model.TaskStatus;
import ru.praktikum.kanban.dto.TaskDto;
import ru.praktikum.kanban.model.Epic;
import ru.praktikum.kanban.model.Task;
import ru.praktikum.kanban.util.AbstractMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.praktikum.kanban.helper.TaskFactory.EPIC;
import static ru.praktikum.kanban.helper.TaskFactory.TASK;

class AbstractMapperTest {

    AbstractMapper<Task, TaskDto> abstractMapper;
    TaskMapper taskMapper = new TaskMapperImpl();

    @BeforeEach
    void setupMapper() {
        abstractMapper = new AbstractMapper<>();
    }

    @Test
    void shouldMapToTaskEntityToTaskDto() {
        abstractMapper.put(Task.class, (value) -> taskMapper.toDto((Task) value));

        Task task = TASK(1);
        TaskDto expected = new TaskDto(
                task.getId(),
                task.getName(),
                task.getDescription(),
                task.getStatus(),
                null,
                null,
                null
        );
        TaskDto actual = abstractMapper.tryMap(task);
        assertEquals(expected, actual);
    }

    @Test
    void shouldBeThrowAndInputIsNull() {
        assertThrows(IllegalArgumentException.class, () -> abstractMapper.tryMap(null));
    }

    @Test
    void shouldBeThrowAndActionIsNull() {
        Executable executable = () -> abstractMapper.put(Task.class, null);
        assertThrows(IllegalArgumentException.class, executable);
    }

    @Test
    void shouldBeThrowAndClassIsNull() {
        Executable executable = () -> abstractMapper.put(null, (value) -> taskMapper.toDto((Task) value));
        assertThrows(IllegalArgumentException.class, executable);
    }

    @Test
    void shouldBeThrowAndAbstractMapperHasNoClassAndNoFunction() {
        Epic epic = EPIC(1);
        assertThrows(IllegalArgumentException.class, () -> abstractMapper.tryMap(epic));
    }
}
