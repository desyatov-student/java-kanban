package ru.praktikum.kanban.service.http;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import ru.praktikum.kanban.dto.CreateTaskDto;
import ru.praktikum.kanban.dto.TaskDto;
import ru.praktikum.kanban.dto.UpdateTaskDto;
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

public class TasksHandler implements HttpHandler {

    private final TaskManager taskManager;
    private final EndpointHandler endpointHandler;
    private final Gson gson;
    private static final Logger logger = Logger.getLogger(TasksHandler.class);

    public TasksHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
        this.endpointHandler = new EndpointHandler()
                .handle(RequestMethod.GET, "/tasks", this::getTasks)
                .handle(RequestMethod.GET, "/tasks/{id}", this::getTask)
                .handle(RequestMethod.POST, "/tasks", this::createTask)
                .handle(RequestMethod.DELETE, "/tasks/{id}", this::removeTask)
                .handle(RequestMethod.PATCH, "/tasks/{id}", this::updateTask);

        this.gson = GsonFactory.taskGson();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        endpointHandler.handle(exchange);
    }

    private void getTasks(HttpExchange exchange) throws IOException {
        String tasksJson = gson.toJson(taskManager.getAllTasks());
        endpointHandler.writeResponse(exchange, tasksJson, SC_OK);
    }

    private void getTask(Map<String, String> params, HttpExchange exchange) throws IOException {
        int taskId;
        try {
            taskId = Preconditions.getIntValue("id", params);
        } catch (PreconditionsException e) {
            endpointHandler.writeResponse(exchange, SC_BAD_REQUEST);
            return;
        }
        Optional<TaskDto> taskOpt = taskManager.getTask(taskId);
        if (taskOpt.isEmpty()) {
            endpointHandler.writeResponse(exchange, SC_NOT_FOUND);
            return;
        }
        String taskJson = gson.toJson(taskOpt.get());
        endpointHandler.writeResponse(exchange, taskJson, SC_OK);
    }

    private void createTask(HttpExchange exchange) throws IOException {
        String body = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
        try {
            if (body.isBlank()) {
                throw new PreconditionsException("Body is empty");
            }
            CreateTaskDto createTaskDto = gson.fromJson(body, CreateTaskDto.class);
            Preconditions.checkRequiredValues(createTaskDto);
            TaskDto taskDto = taskManager.createTask(createTaskDto);
            String taskJson = gson.toJson(taskDto);
            logger.info("createTask. success");
            endpointHandler.writeResponse(exchange, taskJson, SC_CREATED);
        } catch (TaskValidationException e) {
            logger.error("createTask. Could not create task", e);
            endpointHandler.writeResponse(exchange, SC_NOT_ACCEPTABLE);
        } catch (JsonSyntaxException | PreconditionsException e) {
            logger.error("createTask. Could not parse body: " + body, e);
            endpointHandler.writeResponse(exchange, SC_BAD_REQUEST);
        }
    }

    private void updateTask(Map<String, String> params, HttpExchange exchange) throws IOException {
        String body = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
        try {
            if (body.isBlank()) {
                throw new PreconditionsException("Body is empty");
            }
            int taskId = Preconditions.getIntValue("id", params);
            UpdateTaskDto updateTaskDto = gson.fromJson(body, UpdateTaskDto.class);
            Preconditions.checkEmpty(updateTaskDto);
            Optional<TaskDto> taskDtoOpt = taskManager.updateTask(taskId, updateTaskDto);
            if (taskDtoOpt.isEmpty()) {
                endpointHandler.writeResponse(exchange, SC_NOT_FOUND);
                return;
            }
            TaskDto taskDto = taskDtoOpt.get();
            String taskJson = gson.toJson(taskDto);
            logger.info("updateTask. success");
            endpointHandler.writeResponse(exchange, taskJson, SC_OK);
        } catch (TaskValidationException e) {
            logger.error("updateTask. Could not update task", e);
            endpointHandler.writeResponse(exchange, SC_NOT_ACCEPTABLE);
        } catch (JsonSyntaxException | PreconditionsException e) {
            logger.error("updateTask. Could not parse body: " + body, e);
            endpointHandler.writeResponse(exchange, SC_BAD_REQUEST);
        }
    }

    private void removeTask(Map<String, String> params, HttpExchange exchange) throws IOException {
        int taskId;
        try {
            taskId = Preconditions.getIntValue("id", params);
        } catch (PreconditionsException e) {
            endpointHandler.writeResponse(exchange, SC_BAD_REQUEST);
            return;
        }
        taskManager.removeTask(taskId);
        endpointHandler.writeResponse(exchange, SC_OK);
    }
}
