package ru.praktikum.kanban.service.http;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import ru.praktikum.kanban.dto.CreateTaskDto;
import ru.praktikum.kanban.dto.UpdateTaskDto;
import ru.praktikum.kanban.exception.PreconditionsException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.praktikum.kanban.helper.TaskFactory.CREATE_TASK;
import static ru.praktikum.kanban.helper.TaskFactory.UPDATE_TASK;

class PreconditionsTest {

    Gson gson = GsonFactory.taskGson();

    @Test
    void checkState_DoesNotThrowException_CreateTaskHasNameAndDescription() {
        // Given
        // Then
        assertDoesNotThrow(() -> Preconditions.checkState(CREATE_TASK));
    }

    @Test
    void checkState_ThrowsPreconditionsException_CreateTaskDoesNotHaveDescription() {
        // Given
        CreateTaskDto createTaskDto = gson.fromJson("{\"name\":\"name\"}", CreateTaskDto.class);

        // When

        PreconditionsException exception = assertThrows(
                PreconditionsException.class,
                () -> Preconditions.checkState(createTaskDto)
        );

        // Then
        assertEquals("Property description is null", exception.getMessage());
    }

    @Test
    void checkState_ThrowsPreconditionsException_CreateTaskDoesNotHaveName() {
        // Given
        CreateTaskDto createTaskDto = gson.fromJson("{\"description\":\"description\"}", CreateTaskDto.class);

        // When

        PreconditionsException exception = assertThrows(
                PreconditionsException.class,
                () -> Preconditions.checkState(createTaskDto)
        );

        // Then
        assertEquals("Property name is null", exception.getMessage());
    }

    @Test
    void testCheckState_DoesNotThrowException_UpdateTaskHasAnyValue() {
        // Given
        // Then
        assertDoesNotThrow(() -> Preconditions.checkState(UPDATE_TASK));
    }

    @Test
    void testCheckState_ThrowsPreconditionsException_UpdateTaskDoesNotHaveValues() {
        // Given
        UpdateTaskDto updateTaskDto = gson.fromJson("{}", UpdateTaskDto.class);

        // When

        PreconditionsException exception = assertThrows(
                PreconditionsException.class,
                () -> Preconditions.checkState(updateTaskDto)
        );

        // Then
        assertEquals("UpdateTaskDto is empty", exception.getMessage());
    }
}