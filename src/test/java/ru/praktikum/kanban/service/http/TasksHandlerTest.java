package ru.praktikum.kanban.service.http;

import com.google.gson.Gson;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.praktikum.kanban.dto.CreateTaskDto;
import ru.praktikum.kanban.dto.TaskDto;
import ru.praktikum.kanban.dto.UpdateTaskDto;
import ru.praktikum.kanban.exception.TaskValidationException;
import ru.praktikum.kanban.helper.UriBuilder;
import ru.praktikum.kanban.service.TaskManager;
import ru.praktikum.kanban.type.adapter.TaskDtoListTypeToken;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR;
import static org.apache.http.HttpStatus.SC_NOT_ACCEPTABLE;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.praktikum.kanban.helper.TaskFactory.CREATE_TASK;
import static ru.praktikum.kanban.helper.TaskFactory.TASK_DTO;
import static ru.praktikum.kanban.helper.TaskFactory.UPDATE_TASK;

@ExtendWith(MockitoExtension.class)
public class TasksHandlerTest {

    @Mock
    TaskManager taskManager;
    HttpTaskServer httpServer;
    HttpClient client = HttpClient.newHttpClient();
    Gson gson = GsonFactory.taskGson();
    UriBuilder uriBuilder = new UriBuilder();

    @BeforeEach
    void beforeEach() {
        httpServer = new HttpTaskServer(taskManager);
        httpServer.start();
    }

    @AfterEach
    void afterEach() {
        httpServer.stop(0);
    }

    @Test
    void getAllTasks_ReturnTasksAndStatus200_TaskManagerHasTasksAndNoError() throws IOException, InterruptedException {
        // Given

        List<TaskDto> tasks = List.of(TASK_DTO(1), TASK_DTO(2));
        Mockito.when(taskManager.getAllTasks()).thenReturn(tasks);

        URI uri = uriBuilder.create("/tasks");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

        // When

        HttpResponse<String> response = client.send(request, handler);
        List<TaskDto> actualTasks = gson.fromJson(response.body(), new TaskDtoListTypeToken().getType());

        // Then

        assertEquals(SC_OK, response.statusCode());
        assertEquals(tasks, actualTasks);
        Mockito.verify(taskManager).getAllTasks();
        Mockito.verifyNoMoreInteractions(taskManager);
    }

    @Test
    void getTask_ReturnTaskAndStatus200_TaskExistsAndNoError() throws IOException, InterruptedException {
        // Given

        int id = 1;
        TaskDto task = TASK_DTO(id);
        Mockito.when(taskManager.getTask(id)).thenReturn(Optional.of(task));

        URI uri = uriBuilder.create("/tasks/" + id);
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

        // When

        HttpResponse<String> response = client.send(request, handler);
        TaskDto actualTask = gson.fromJson(response.body(), TaskDto.class);

        // Then

        assertEquals(SC_OK, response.statusCode());
        assertEquals(task, actualTask);
        Mockito.verify(taskManager).getTask(id);
        Mockito.verifyNoMoreInteractions(taskManager);
    }

    @Test
    void getTask_ReturnEmptyResponseAndStatus404_TaskDoesNotExist() throws IOException, InterruptedException {
        // Given

        int id = 1;
        Mockito.when(taskManager.getTask(id)).thenReturn(Optional.empty());

        URI uri = uriBuilder.create("/tasks/" + id);
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

        // When

        HttpResponse<String> response = client.send(request, handler);

        // Then

        assertEquals(SC_NOT_FOUND, response.statusCode());
        assertTrue(response.body().isEmpty());
        Mockito.verify(taskManager).getTask(id);
        Mockito.verifyNoMoreInteractions(taskManager);
    }

    @Test
    void createTask_ReturnTaskAndStatus200_TaskSavedAndNoError() throws IOException, InterruptedException, TaskValidationException {
        // Given

        int id = 1;
        CreateTaskDto createTaskDto = CREATE_TASK;
        String createTaskDtoJson = gson.toJson(createTaskDto);
        TaskDto taskDto = TASK_DTO(id);
        Mockito.when(taskManager.createTask(createTaskDto)).thenReturn(taskDto);

        URI uri = uriBuilder.create("/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(createTaskDtoJson))
                .uri(uri)
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

        // When

        HttpResponse<String> response = client.send(request, handler);
        TaskDto actualTask = gson.fromJson(response.body(), TaskDto.class);

        // Then

        assertEquals(SC_CREATED, response.statusCode());
        assertEquals(taskDto, actualTask);
        Mockito.verify(taskManager).createTask(createTaskDto);
        Mockito.verifyNoMoreInteractions(taskManager);
    }

    @Test
    void createTask_ReturnEmptyResponseAndStatus406_TaskHasTimeIntersection() throws IOException, InterruptedException, TaskValidationException {
        // Given

        CreateTaskDto createTaskDto = CREATE_TASK;
        String createTaskDtoJson = gson.toJson(createTaskDto);
        Mockito.when(taskManager.createTask(createTaskDto)).thenThrow(new TaskValidationException());

        URI uri = uriBuilder.create("/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(createTaskDtoJson))
                .uri(uri)
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

        // When

        HttpResponse<String> response = client.send(request, handler);

        // Then

        assertEquals(SC_NOT_ACCEPTABLE, response.statusCode());
        assertTrue(response.body().isEmpty());
        Mockito.verify(taskManager).createTask(createTaskDto);
        Mockito.verifyNoMoreInteractions(taskManager);
    }

    @Test
    void createTask_ReturnEmptyResponseAndStatus400_BodyIsEmpty() throws IOException, InterruptedException, TaskValidationException {
        // Given

        URI uri = uriBuilder.create("/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.noBody())
                .uri(uri)
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

        // When

        HttpResponse<String> response = client.send(request, handler);

        // Then

        assertEquals(SC_BAD_REQUEST, response.statusCode());
        assertTrue(response.body().isEmpty());
        Mockito.verifyNoMoreInteractions(taskManager);
    }

    @Test
    void createTask_ReturnEmptyResponseAndStatus500_TaskManagerThrowException() throws IOException, InterruptedException, TaskValidationException {
        // Given

        CreateTaskDto createTaskDto = CREATE_TASK;
        String createTaskDtoJson = gson.toJson(createTaskDto);
        Mockito.when(taskManager.createTask(createTaskDto)).thenThrow(new RuntimeException());

        URI uri = uriBuilder.create("/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(createTaskDtoJson))
                .uri(uri)
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

        // When

        HttpResponse<String> response = client.send(request, handler);

        // Then

        assertEquals(SC_INTERNAL_SERVER_ERROR, response.statusCode());
        assertTrue(response.body().isEmpty());
        Mockito.verifyNoMoreInteractions(taskManager);
    }

    @Test
    void updateTask_ReturnUpdatedTaskAndStatus200_TaskExists() throws IOException, InterruptedException, TaskValidationException {
        // Given

        int id = 1;
        UpdateTaskDto updateTaskDto = UPDATE_TASK;
        String updateTaskDtoJson = gson.toJson(updateTaskDto);
        TaskDto taskDto = TASK_DTO(id);
        Mockito.when(taskManager.updateTask(id, updateTaskDto)).thenReturn(Optional.of(taskDto));

        URI uri = uriBuilder.create("/tasks/" + id);
        HttpRequest request = HttpRequest.newBuilder()
                .method("PATCH", HttpRequest.BodyPublishers.ofString(updateTaskDtoJson))
                .uri(uri)
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

        // When

        HttpResponse<String> response = client.send(request, handler);
        TaskDto actualTask = gson.fromJson(response.body(), TaskDto.class);

        // Then

        assertEquals(SC_OK, response.statusCode());
        assertEquals(taskDto, actualTask);
        Mockito.verify(taskManager).updateTask(id, updateTaskDto);
        Mockito.verifyNoMoreInteractions(taskManager);
    }

    @Test
    void updateTask_ReturnEmptyResponseAndStatus404_TaskDoesNotExist() throws IOException, InterruptedException, TaskValidationException {
        // Given

        int id = 1;
        UpdateTaskDto updateTaskDto = UPDATE_TASK;
        String updateTaskDtoJson = gson.toJson(updateTaskDto);
        Mockito.when(taskManager.updateTask(id, updateTaskDto)).thenReturn(Optional.empty());

        URI uri = uriBuilder.create("/tasks/" + id);
        HttpRequest request = HttpRequest.newBuilder()
                .method("PATCH", HttpRequest.BodyPublishers.ofString(updateTaskDtoJson))
                .uri(uri)
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

        // When

        HttpResponse<String> response = client.send(request, handler);

        // Then

        assertEquals(SC_NOT_FOUND, response.statusCode());
        assertTrue(response.body().isEmpty());
        Mockito.verify(taskManager).updateTask(id, updateTaskDto);
        Mockito.verifyNoMoreInteractions(taskManager);
    }

    @Test
    void updateTask_ReturnEmptyResponseAndStatus406_TaskHasTimeIntersection() throws IOException, InterruptedException, TaskValidationException {
        // Given

        int id = 1;
        UpdateTaskDto updateTaskDto = UPDATE_TASK;
        String updateTaskDtoJson = gson.toJson(updateTaskDto);
        Mockito.when(taskManager.updateTask(id, updateTaskDto)).thenThrow(new TaskValidationException());

        URI uri = uriBuilder.create("/tasks/" + id);
        HttpRequest request = HttpRequest.newBuilder()
                .method("PATCH", HttpRequest.BodyPublishers.ofString(updateTaskDtoJson))
                .uri(uri)
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

        // When

        HttpResponse<String> response = client.send(request, handler);

        // Then

        assertEquals(SC_NOT_ACCEPTABLE, response.statusCode());
        assertTrue(response.body().isEmpty());
        Mockito.verify(taskManager).updateTask(id, updateTaskDto);
        Mockito.verifyNoMoreInteractions(taskManager);
    }

    @Test
    void updateTask_ReturnEmptyResponseAndStatus400_BodyIsEmpty() throws IOException, InterruptedException, TaskValidationException {
        // Given

        int id = 1;
        URI uri = uriBuilder.create("/tasks/" + id);
        HttpRequest request = HttpRequest.newBuilder()
                .method("PATCH", HttpRequest.BodyPublishers.noBody())
                .uri(uri)
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

        // When

        HttpResponse<String> response = client.send(request, handler);

        // Then

        assertEquals(SC_BAD_REQUEST, response.statusCode());
        assertTrue(response.body().isEmpty());
        Mockito.verifyNoMoreInteractions(taskManager);
    }
}
