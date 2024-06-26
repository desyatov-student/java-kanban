package ru.praktikum.kanban.service.http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import ru.praktikum.kanban.service.TaskManager;

import static org.apache.http.HttpStatus.SC_OK;

public class PrioritizedHandler implements HttpHandler {

    private final TaskManager taskManager;
    private final EndpointHandler endpointHandler;
    private final Gson gson;

    public PrioritizedHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
        this.endpointHandler = new EndpointHandler()
                .handle(RequestMethod.GET, "/prioritized", this::getPrioritizedTasks);

        this.gson = GsonFactory.gsonWithCustomSerializer();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        endpointHandler.handle(exchange);
    }

    private void getPrioritizedTasks(HttpExchange exchange) throws IOException {
        String prioritizedJson = gson.toJson(taskManager.getPrioritizedTasks());
        endpointHandler.writeResponse(exchange, prioritizedJson, SC_OK);
    }
}
