package ru.praktikum.kanban;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import ru.praktikum.kanban.model.TaskStatus;
import ru.praktikum.kanban.model.dto.response.TaskDto;
import ru.praktikum.kanban.model.entity.Epic;
import ru.praktikum.kanban.model.entity.Task;
import ru.praktikum.kanban.service.mapper.TaskMapper;
import ru.praktikum.kanban.model.mapper.TaskMapperImpl;
import ru.praktikum.kanban.util.AbstractMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

        Task task = new Task(1, "", "", TaskStatus.NEW);
        TaskDto expected = new TaskDto(task.getId(), task.name, task.description, task.status);
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
        Epic epic = new Epic(1, "", "", TaskStatus.NEW, List.of());
        assertThrows(IllegalArgumentException.class, () -> abstractMapper.tryMap(epic));
    }
}
