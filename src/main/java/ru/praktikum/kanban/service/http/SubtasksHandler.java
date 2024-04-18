package ru.praktikum.kanban.service.http;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import ru.praktikum.kanban.dto.CreateSubtaskDto;
import ru.praktikum.kanban.dto.SubtaskDto;
import ru.praktikum.kanban.dto.UpdateSubtaskDto;
import ru.praktikum.kanban.exception.PreconditionsException;
import ru.praktikum.kanban.exception.TaskValidationException;
import ru.praktikum.kanban.service.TaskManager;
import ru.praktikum.kanban.util.Logger;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_NOT_ACCEPTABLE;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.apache.http.HttpStatus.SC_OK;
import static ru.praktikum.kanban.service.http.EndpointHandler.DEFAULT_CHARSET;

public class SubtasksHandler implements HttpHandler {

    private final TaskManager taskManager;
    private final EndpointHandler endpointHandler;
    private final Gson gson;
    private static final Logger logger = Logger.getLogger(SubtasksHandler.class);

    public SubtasksHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
        this.endpointHandler = new EndpointHandler()
                .handle(RequestMethod.GET, "/subtasks", this::getSubtasks)
                .handle(RequestMethod.GET, "/subtasks/{id}", this::getSubtask)
                .handle(RequestMethod.POST, "/subtasks/{epicId}", this::createSubtask)
                .handle(RequestMethod.DELETE, "/subtasks/{id}", this::removeSubtask)
                .handle(RequestMethod.PATCH, "/subtasks/{id}", this::updateSubtask);

        this.gson = GsonFactory.taskGson();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        endpointHandler.handle(exchange);
    }

    private void getSubtasks(HttpExchange exchange) throws IOException {
        String subtasksJson = gson.toJson(taskManager.getAllSubtasks());
        endpointHandler.writeResponse(exchange, subtasksJson, SC_OK);
    }

    private void getSubtask(Map<String, String> params, HttpExchange exchange) throws IOException {
        int subtaskId;
        try {
            subtaskId = Preconditions.getIntValue("id", params);
        } catch (PreconditionsException e) {
            endpointHandler.writeResponse(exchange, SC_BAD_REQUEST);
            return;
        }
        Optional<SubtaskDto> subtaskOpt = taskManager.getSubtask(subtaskId);
        if (subtaskOpt.isEmpty()) {
            endpointHandler.writeResponse(exchange, SC_NOT_FOUND);
            return;
        }
        String subtaskJson = gson.toJson(subtaskOpt.get());
        endpointHandler.writeResponse(exchange, subtaskJson, SC_OK);
    }

    private void createSubtask(Map<String, String> params, HttpExchange exchange) throws IOException {
        String body = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
        try {
            if (body.isBlank()) {
                throw new PreconditionsException("Body is empty");
            }
            int epicId = Preconditions.getIntValue("epicId", params);
            CreateSubtaskDto createSubtaskDto = gson.fromJson(body, CreateSubtaskDto.class);
            Preconditions.checkRequiredValues(createSubtaskDto);
            SubtaskDto subtaskDto = taskManager.createSubtask(epicId, createSubtaskDto);
            String subtaskJson = gson.toJson(subtaskDto);
            logger.info("createSubtask. success");
            endpointHandler.writeResponse(exchange, subtaskJson, SC_CREATED);
        } catch (TaskValidationException e) {
            logger.error("createSubtask. Could not create task", e);
            endpointHandler.writeResponse(exchange, SC_NOT_ACCEPTABLE);
        } catch (JsonSyntaxException | PreconditionsException e) {
            logger.error("createSubtask. Could not parse body: " + body, e);
            endpointHandler.writeResponse(exchange, SC_BAD_REQUEST);
        }
    }

    private void removeSubtask(Map<String, String> params, HttpExchange exchange) throws IOException {
        int subtaskId;
        try {
            subtaskId = Preconditions.getIntValue("id", params);
        } catch (PreconditionsException e) {
            endpointHandler.writeResponse(exchange, SC_BAD_REQUEST);
            return;
        }
        taskManager.removeSubtask(subtaskId);
        endpointHandler.writeResponse(exchange, SC_OK);
    }

    private void updateSubtask(Map<String, String> params, HttpExchange exchange) throws IOException {
        String body = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
        try {
            if (body.isBlank()) {
                throw new PreconditionsException("Body is empty");
            }
            int subtaskId = Preconditions.getIntValue("id", params);
            UpdateSubtaskDto updateSubtaskDto = gson.fromJson(body, UpdateSubtaskDto.class);
            Preconditions.checkEmpty(updateSubtaskDto);
            Optional<SubtaskDto> subtaskDtoOpt = taskManager.updateSubtask(subtaskId, updateSubtaskDto);
            if (subtaskDtoOpt.isEmpty()) {
                endpointHandler.writeResponse(exchange, SC_NOT_FOUND);
                return;
            }
            SubtaskDto subtaskDto = subtaskDtoOpt.get();
            String subtaskJson = gson.toJson(subtaskDto);
            logger.info("updateSubtask. success");
            endpointHandler.writeResponse(exchange, subtaskJson, SC_OK);
        } catch (TaskValidationException e) {
            logger.error("updateSubtask. Could not update task", e);
            endpointHandler.writeResponse(exchange, SC_NOT_ACCEPTABLE);
        } catch (JsonSyntaxException | PreconditionsException e) {
            logger.error("updateSubtask. Could not parse body: " + body, e);
            endpointHandler.writeResponse(exchange, SC_BAD_REQUEST);
        }
    }
}
