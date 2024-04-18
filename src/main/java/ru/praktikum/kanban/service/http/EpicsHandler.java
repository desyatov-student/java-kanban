package ru.praktikum.kanban.service.http;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import ru.praktikum.kanban.dto.CreateEpicDto;
import ru.praktikum.kanban.dto.EpicDto;
import ru.praktikum.kanban.dto.TaskDto;
import ru.praktikum.kanban.dto.UpdateEpicDto;
import ru.praktikum.kanban.exception.PreconditionsException;
import ru.praktikum.kanban.service.TaskManager;
import ru.praktikum.kanban.util.Logger;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.apache.http.HttpStatus.SC_OK;
import static ru.praktikum.kanban.service.http.EndpointHandler.DEFAULT_CHARSET;

public class EpicsHandler implements HttpHandler {

    private final TaskManager taskManager;
    private final EndpointHandler endpointHandler;
    private final Gson gson;
    private static final Logger logger = Logger.getLogger(TasksHandler.class);

    public EpicsHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
        this.endpointHandler = new EndpointHandler()
                .handle(RequestMethod.GET, "/epics", this::getEpics)
                .handle(RequestMethod.GET, "/epics/{id}", this::getEpic)
                .handle(RequestMethod.GET, "/epics/{id}/subtasks", this::getEpicSubtasks)
                .handle(RequestMethod.POST, "/epics", this::createEpic)
                .handle(RequestMethod.DELETE, "/epics/{id}", this::removeEpic)
                .handle(RequestMethod.PATCH, "/epics/{id}", this::updateEpic);

        this.gson = GsonFactory.taskGson();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        endpointHandler.handle(exchange);
    }

    private void getEpics(HttpExchange exchange) throws IOException {
        String tasksJson = gson.toJson(taskManager.getAllEpics());
        endpointHandler.writeResponse(exchange, tasksJson, SC_OK);
    }

    private void getEpic(Map<String, String> params, HttpExchange exchange) throws IOException {
        int epicId;
        try {
            epicId = Preconditions.getIntValue("id", params);
        } catch (PreconditionsException e) {
            endpointHandler.writeResponse(exchange, SC_BAD_REQUEST);
            return;
        }
        Optional<EpicDto> epicOpt = taskManager.getEpic(epicId);
        if (epicOpt.isEmpty()) {
            endpointHandler.writeResponse(exchange, SC_NOT_FOUND);
            return;
        }
        String epicJson = gson.toJson(epicOpt.get());
        endpointHandler.writeResponse(exchange, epicJson, SC_OK);
    }

    private void getEpicSubtasks(Map<String, String> params, HttpExchange exchange) throws IOException {
        int epicId;
        try {
            epicId = Preconditions.getIntValue("id", params);
        } catch (PreconditionsException e) {
            endpointHandler.writeResponse(exchange, SC_BAD_REQUEST);
            return;
        }
        String subtasksJson = gson.toJson(taskManager.getSubtasksWithEpicId(epicId));
        endpointHandler.writeResponse(exchange, subtasksJson, SC_OK);
    }

    private void createEpic(HttpExchange exchange) throws IOException {
        String body = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
        try {
            if (body.isBlank()) {
                throw new PreconditionsException("Body is empty");
            }
            CreateEpicDto createEpicDto = gson.fromJson(body, CreateEpicDto.class);
            Preconditions.checkRequiredValues(createEpicDto);
            TaskDto taskDto = taskManager.createEpic(createEpicDto);
            String taskJson = gson.toJson(taskDto);
            logger.info("createTask. success");
            endpointHandler.writeResponse(exchange, taskJson, SC_CREATED);
        } catch (JsonSyntaxException | PreconditionsException e) {
            logger.error("createTask. Could not parse body: " + body, e);
            endpointHandler.writeResponse(exchange, SC_BAD_REQUEST);
        }
    }

    private void updateEpic(Map<String, String> params, HttpExchange exchange) throws IOException {
        String body = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
        try {
            if (body.isBlank()) {
                throw new PreconditionsException("Body is empty");
            }
            int epicId = Preconditions.getIntValue("id", params);
            UpdateEpicDto updateEpicDto = gson.fromJson(body, UpdateEpicDto.class);
            Preconditions.checkEmpty(updateEpicDto);
            Optional<EpicDto> epicDtoOpt = taskManager.updateEpic(epicId, updateEpicDto);
            if (epicDtoOpt.isEmpty()) {
                endpointHandler.writeResponse(exchange, SC_NOT_FOUND);
                return;
            }
            EpicDto epicDto = epicDtoOpt.get();
            String epicJson = gson.toJson(epicDto);
            logger.info("updateTask. success");
            endpointHandler.writeResponse(exchange, epicJson, SC_OK);
        } catch (JsonSyntaxException | PreconditionsException e) {
            logger.error("updateTask. Could not parse body: " + body, e);
            endpointHandler.writeResponse(exchange, SC_BAD_REQUEST);
        }
    }

    private void removeEpic(Map<String, String> params, HttpExchange exchange) throws IOException {
        int epicId;
        try {
            epicId = Preconditions.getIntValue("id", params);
        } catch (PreconditionsException e) {
            endpointHandler.writeResponse(exchange, SC_BAD_REQUEST);
            return;
        }
        taskManager.removeEpic(epicId);
        endpointHandler.writeResponse(exchange, SC_OK);
    }
}
