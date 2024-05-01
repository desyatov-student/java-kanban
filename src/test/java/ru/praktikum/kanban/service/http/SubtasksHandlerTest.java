package ru.praktikum.kanban.service.http;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.praktikum.kanban.dto.CreateSubtaskDto;
import ru.praktikum.kanban.dto.SubtaskDto;
import ru.praktikum.kanban.dto.UpdateSubtaskDto;
import ru.praktikum.kanban.exception.TaskValidationException;
import ru.praktikum.kanban.helper.Response;
import ru.praktikum.kanban.type.adapter.SubtaskDtoListTypeToken;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR;
import static org.apache.http.HttpStatus.SC_NOT_ACCEPTABLE;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.praktikum.kanban.helper.TaskFactory.CREATE_SUBTASK;
import static ru.praktikum.kanban.helper.TaskFactory.SUBTASK_DTO;
import static ru.praktikum.kanban.helper.TaskFactory.UPDATE_SUBTASK;

@ExtendWith(MockitoExtension.class)
public class SubtasksHandlerTest extends ApiHandlerTest {

    @Test
    void getAllSubtasks_ReturnTasksAndStatus200_TaskManagerHasSubtasksAndNoError() throws IOException, InterruptedException {
        // Given

        List<SubtaskDto> subtasks = List.of(SUBTASK_DTO(1), SUBTASK_DTO(2));
        Mockito.when(taskManager.getAllSubtasks()).thenReturn(subtasks);

        // When

        List<SubtaskDto> actualSubtasks = requestBuilder()
                .GET()
                .path("/subtasks")
                .acceptableStatusCode(SC_OK)
                .send().decodeBody(new SubtaskDtoListTypeToken());

        // Then

        assertEquals(subtasks, actualSubtasks);
        Mockito.verify(taskManager).getAllSubtasks();
        Mockito.verifyNoMoreInteractions(taskManager);
    }

    @Test
    void getSubtask_ReturnTaskAndStatus200_SubtaskExistsAndNoError() throws IOException, InterruptedException {
        // Given

        int id = 1;
        SubtaskDto subtask = SUBTASK_DTO(id);
        Mockito.when(taskManager.getSubtask(id)).thenReturn(Optional.of(subtask));

        // When

        SubtaskDto actualSubtask = requestBuilder()
                .GET()
                .path("/subtasks/" + id)
                .acceptableStatusCode(SC_OK)
                .send().decodeBody(SubtaskDto.class);

        // Then

        assertEquals(subtask, actualSubtask);
        Mockito.verify(taskManager).getSubtask(id);
        Mockito.verifyNoMoreInteractions(taskManager);
    }

    @Test
    void getSubtask_ReturnEmptyResponseAndStatus404_SubtaskDoesNotExist() throws IOException, InterruptedException {
        // Given

        int id = 1;
        Mockito.when(taskManager.getSubtask(id)).thenReturn(Optional.empty());

        // When

        Response response = requestBuilder()
                .GET()
                .path("/subtasks/" + id)
                .acceptableStatusCode(SC_NOT_FOUND)
                .send();

        // Then

        assertTrue(response.body().isEmpty());
        Mockito.verify(taskManager).getSubtask(id);
        Mockito.verifyNoMoreInteractions(taskManager);
    }

    @Test
    void createSubtask_ReturnSubtaskAndStatus201_TaskSavedAndNoError() throws IOException, InterruptedException, TaskValidationException {
        // Given

        int epicId = 5;
        CreateSubtaskDto createSubtaskDto = CREATE_SUBTASK;
        SubtaskDto subtaskDto = SUBTASK_DTO(1);
        Mockito.when(taskManager.createSubtask(epicId, createSubtaskDto)).thenReturn(subtaskDto);

        // When

        SubtaskDto actualSubtask = requestBuilder()
                .POST()
                .path("/subtasks/" + epicId)
                .requestBody(createSubtaskDto)
                .acceptableStatusCode(SC_CREATED)
                .send().decodeBody(SubtaskDto.class);

        // Then

        assertEquals(subtaskDto, actualSubtask);
        Mockito.verify(taskManager).createSubtask(epicId, createSubtaskDto);
        Mockito.verifyNoMoreInteractions(taskManager);
    }

    @Test
    void createSubtask_ReturnEmptyResponseAndStatus406_SubtaskHasTimeIntersection() throws IOException, InterruptedException, TaskValidationException {
        // Given

        int epicId = 5;
        CreateSubtaskDto createSubtaskDto = CREATE_SUBTASK;
        Mockito.when(taskManager.createSubtask(epicId, createSubtaskDto)).thenThrow(new TaskValidationException());

        // When

        Response response = requestBuilder()
                .POST()
                .path("/subtasks/" + epicId)
                .requestBody(createSubtaskDto)
                .acceptableStatusCode(SC_NOT_ACCEPTABLE)
                .send();

        // Then

        assertTrue(response.body().isEmpty());
        Mockito.verify(taskManager).createSubtask(epicId, createSubtaskDto);
        Mockito.verifyNoMoreInteractions(taskManager);
    }

    @Test
    void createTask_ReturnEmptyResponseAndStatus400_BodyIsEmpty() throws IOException, InterruptedException, TaskValidationException {
        // Given

        int epicId = 5;

        // When

        Response response = requestBuilder()
                .POST()
                .path("/subtasks/" + epicId)
                .acceptableStatusCode(SC_BAD_REQUEST)
                .send();

        // Then

        assertTrue(response.body().isEmpty());
        Mockito.verifyNoMoreInteractions(taskManager);
    }

    @Test
    void createSubtask_ReturnEmptyResponseAndStatus500_TaskManagerThrowException() throws IOException, InterruptedException, TaskValidationException {
        // Given

        int epicId = 5;
        CreateSubtaskDto createSubtaskDto = CREATE_SUBTASK;
        Mockito.when(taskManager.createSubtask(epicId, createSubtaskDto)).thenThrow(new RuntimeException());

        // When

        Response response = requestBuilder()
                .POST()
                .path("/subtasks/" + epicId)
                .requestBody(createSubtaskDto)
                .acceptableStatusCode(SC_INTERNAL_SERVER_ERROR)
                .send();

        // Then

        assertTrue(response.body().isEmpty());
        Mockito.verify(taskManager).createSubtask(epicId, createSubtaskDto);
        Mockito.verifyNoMoreInteractions(taskManager);
    }

    @Test
    void updateSubtask_ReturnUpdatedTaskAndStatus200_SubtaskExists() throws IOException, InterruptedException, TaskValidationException {
        // Given

        int id = 1;
        UpdateSubtaskDto updateSubtaskDto = UPDATE_SUBTASK();
        SubtaskDto subtaskDto = SUBTASK_DTO(1);
        Mockito.when(taskManager.updateSubtask(id, updateSubtaskDto)).thenReturn(Optional.of(subtaskDto));

        // When

        SubtaskDto actualSubtask = requestBuilder()
                .PATCH()
                .path("/subtasks/" + id)
                .requestBody(updateSubtaskDto)
                .acceptableStatusCode(SC_OK)
                .send().decodeBody(SubtaskDto.class);

        // Then

        assertEquals(subtaskDto, actualSubtask);
        Mockito.verify(taskManager).updateSubtask(id, updateSubtaskDto);
        Mockito.verifyNoMoreInteractions(taskManager);
    }

    @Test
    void updateSubtask_ReturnEmptyResponseAndStatus404_SubtaskDoesNotExist() throws IOException, InterruptedException, TaskValidationException {
        // Given

        int id = 1;
        UpdateSubtaskDto updateSubtaskDto = UPDATE_SUBTASK();
        Mockito.when(taskManager.updateSubtask(id, updateSubtaskDto)).thenReturn(Optional.empty());

        // When

        Response response = requestBuilder()
                .PATCH()
                .path("/subtasks/" + id)
                .requestBody(updateSubtaskDto)
                .acceptableStatusCode(SC_NOT_FOUND)
                .send();

        // Then

        assertTrue(response.body().isEmpty());
        Mockito.verify(taskManager).updateSubtask(id, updateSubtaskDto);
        Mockito.verifyNoMoreInteractions(taskManager);
    }

    @Test
    void updateSubtask_ReturnEmptyResponseAndStatus406_SubtaskHasTimeIntersection() throws IOException, InterruptedException, TaskValidationException {
        // Given

        int id = 1;
        UpdateSubtaskDto updateSubtaskDto = UPDATE_SUBTASK();
        Mockito.when(taskManager.updateSubtask(id, updateSubtaskDto)).thenThrow(new TaskValidationException());

        // When

        Response response = requestBuilder()
                .PATCH()
                .path("/subtasks/" + id)
                .requestBody(updateSubtaskDto)
                .acceptableStatusCode(SC_NOT_ACCEPTABLE)
                .send();

        // Then

        assertTrue(response.body().isEmpty());
        Mockito.verify(taskManager).updateSubtask(id, updateSubtaskDto);
        Mockito.verifyNoMoreInteractions(taskManager);
    }

    @Test
    void updateSubtask_ReturnEmptyResponseAndStatus400_BodyIsEmpty() throws IOException, InterruptedException, TaskValidationException {
        // Given

        int id = 1;

        // When

        Response response = requestBuilder()
                .PATCH()
                .path("/subtasks/" + id)
                .acceptableStatusCode(SC_BAD_REQUEST)
                .send();

        // Then

        assertTrue(response.body().isEmpty());
        Mockito.verifyNoMoreInteractions(taskManager);
    }
}
