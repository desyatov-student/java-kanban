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
import ru.praktikum.kanban.dto.CreateEpicDto;
import ru.praktikum.kanban.dto.EpicDto;
import ru.praktikum.kanban.dto.SubtaskDto;
import ru.praktikum.kanban.dto.UpdateEpicDto;
import ru.praktikum.kanban.exception.TaskValidationException;
import ru.praktikum.kanban.helper.UriBuilder;
import ru.praktikum.kanban.service.TaskManager;
import ru.praktikum.kanban.type.adapter.EpicDtoListTypeToken;
import ru.praktikum.kanban.type.adapter.SubtaskDtoListTypeToken;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.praktikum.kanban.helper.TaskFactory.CREATE_EPIC;
import static ru.praktikum.kanban.helper.TaskFactory.EPIC_DTO;
import static ru.praktikum.kanban.helper.TaskFactory.SUBTASK_DTO;
import static ru.praktikum.kanban.helper.TaskFactory.UPDATE_EPIC;

@ExtendWith(MockitoExtension.class)
public class EpicsHandlerTest {

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
    void getAllEpics_ReturnTasksAndStatus200_TaskManagerHasEpicsAndNoError() throws IOException, InterruptedException {
        // Given

        List<EpicDto> tasks = List.of(EPIC_DTO(1), EPIC_DTO(2));
        Mockito.when(taskManager.getAllEpics()).thenReturn(tasks);

        URI uri = uriBuilder.create("/epics");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

        // When

        HttpResponse<String> response = client.send(request, handler);
        List<EpicDto> actualTasks = gson.fromJson(response.body(), new EpicDtoListTypeToken().getType());

        // Then

        assertEquals(SC_OK, response.statusCode());
        assertEquals(tasks, actualTasks);
        Mockito.verify(taskManager).getAllEpics();
        Mockito.verifyNoMoreInteractions(taskManager);
    }

    @Test
    void getEpic_ReturnTaskAndStatus200_EpicExistsAndNoError() throws IOException, InterruptedException {
        // Given

        int id = 1;
        EpicDto task = EPIC_DTO(id);
        Mockito.when(taskManager.getEpic(id)).thenReturn(Optional.of(task));

        URI uri = uriBuilder.create("/epics/" + id);
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

        // When

        HttpResponse<String> response = client.send(request, handler);
        EpicDto actualTask = gson.fromJson(response.body(), EpicDto.class);

        // Then

        assertEquals(SC_OK, response.statusCode());
        assertEquals(task, actualTask);
        Mockito.verify(taskManager).getEpic(id);
        Mockito.verifyNoMoreInteractions(taskManager);
    }

    @Test
    void getEpicSubtasks_ReturnSubtasksAndStatus200_EpicExistsAndHasSubtasksAndNoError() throws IOException, InterruptedException {
        // Given

        int id = 1;
        List<SubtaskDto> subtasks = List.of(SUBTASK_DTO(1), SUBTASK_DTO(2));
        Mockito.when(taskManager.getSubtasksWithEpicId(id)).thenReturn(subtasks);

        URI uri = uriBuilder.create("/epics/" + id + "/subtasks");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

        // When

        HttpResponse<String> response = client.send(request, handler);
        List<SubtaskDto> actualSubtasks = gson.fromJson(response.body(), new SubtaskDtoListTypeToken());

        // Then

        assertEquals(SC_OK, response.statusCode());
        assertEquals(subtasks, actualSubtasks);
        Mockito.verify(taskManager).getSubtasksWithEpicId(id);
        Mockito.verifyNoMoreInteractions(taskManager);
    }

    @Test
    void getEpic_ReturnEmptyResponseAndStatus404_EpicDoesNotExist() throws IOException, InterruptedException {
        // Given

        int id = 1;
        Mockito.when(taskManager.getEpic(id)).thenReturn(Optional.empty());

        URI uri = uriBuilder.create("/epics/" + id);
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

        // When

        HttpResponse<String> response = client.send(request, handler);

        // Then

        assertEquals(SC_NOT_FOUND, response.statusCode());
        assertTrue(response.body().isEmpty());
        Mockito.verify(taskManager).getEpic(id);
        Mockito.verifyNoMoreInteractions(taskManager);
    }

    @Test
    void createEpic_ReturnEpicAndStatus200_EpicSavedAndNoError() throws IOException, InterruptedException, TaskValidationException {
        // Given

        int id = 1;
        CreateEpicDto createEpicDto = CREATE_EPIC();
        String createEpicDtoJson = gson.toJson(createEpicDto);
        EpicDto epicDto = EPIC_DTO(id);
        Mockito.when(taskManager.createEpic(createEpicDto)).thenReturn(epicDto);

        URI uri = uriBuilder.create("/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(createEpicDtoJson))
                .uri(uri)
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

        // When

        HttpResponse<String> response = client.send(request, handler);
        EpicDto actualTask = gson.fromJson(response.body(), EpicDto.class);

        // Then

        assertEquals(SC_CREATED, response.statusCode());
        assertEquals(epicDto, actualTask);
        Mockito.verify(taskManager).createEpic(createEpicDto);
        Mockito.verifyNoMoreInteractions(taskManager);
    }

    @Test
    void createEpic_ReturnEmptyResponseAndStatus400_BodyIsEmpty() throws IOException, InterruptedException, TaskValidationException {
        // Given

        URI uri = uriBuilder.create("/epics");
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
    void createEpic_ReturnEmptyResponseAndStatus500_TaskManagerThrowException() throws IOException, InterruptedException, TaskValidationException {
        // Given

        CreateEpicDto createEpicDto = CREATE_EPIC();
        String createEpicDtoJson = gson.toJson(createEpicDto);
        Mockito.when(taskManager.createEpic(createEpicDto)).thenThrow(new RuntimeException());

        URI uri = uriBuilder.create("/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(createEpicDtoJson))
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
    void updateEpic_ReturnUpdatedEpicAndStatus200_EpicExists() throws IOException, InterruptedException, TaskValidationException {
        // Given

        int id = 1;
        UpdateEpicDto updateEpicDto = UPDATE_EPIC();
        String updateEpicDtoJson = gson.toJson(updateEpicDto);
        EpicDto epicDto = EPIC_DTO(id);
        Mockito.when(taskManager.updateEpic(id, updateEpicDto)).thenReturn(Optional.of(epicDto));

        URI uri = uriBuilder.create("/epics/" + id);
        HttpRequest request = HttpRequest.newBuilder()
                .method("PATCH", HttpRequest.BodyPublishers.ofString(updateEpicDtoJson))
                .uri(uri)
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

        // When

        HttpResponse<String> response = client.send(request, handler);
        EpicDto actualTask = gson.fromJson(response.body(), EpicDto.class);

        // Then

        assertEquals(SC_OK, response.statusCode());
        assertEquals(epicDto, actualTask);
        Mockito.verify(taskManager).updateEpic(id, updateEpicDto);
        Mockito.verifyNoMoreInteractions(taskManager);
    }

    @Test
    void updateEpic_ReturnEmptyResponseAndStatus404_EpicDoesNotExist() throws IOException, InterruptedException, TaskValidationException {
        // Given

        int id = 1;
        UpdateEpicDto updateEpicDto = UPDATE_EPIC();
        String updateEpicDtoJson = gson.toJson(updateEpicDto);
        Mockito.when(taskManager.updateEpic(id, updateEpicDto)).thenReturn(Optional.empty());

        URI uri = uriBuilder.create("/epics/" + id);
        HttpRequest request = HttpRequest.newBuilder()
                .method("PATCH", HttpRequest.BodyPublishers.ofString(updateEpicDtoJson))
                .uri(uri)
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

        // When

        HttpResponse<String> response = client.send(request, handler);

        // Then

        assertEquals(SC_NOT_FOUND, response.statusCode());
        assertTrue(response.body().isEmpty());
        Mockito.verify(taskManager).updateEpic(id, updateEpicDto);
        Mockito.verifyNoMoreInteractions(taskManager);
    }

    @Test
    void updateTask_ReturnEmptyResponseAndStatus400_BodyIsEmpty() throws IOException, InterruptedException, TaskValidationException {
        // Given

        URI uri = uriBuilder.create("/epics/1");
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
