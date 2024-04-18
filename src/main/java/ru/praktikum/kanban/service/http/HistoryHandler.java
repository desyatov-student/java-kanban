package ru.praktikum.kanban.service.http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import ru.praktikum.kanban.service.TaskManager;
import ru.praktikum.kanban.util.Logger;

import static org.apache.http.HttpStatus.SC_OK;

public class HistoryHandler implements HttpHandler {

    private final TaskManager taskManager;
    private final EndpointHandler endpointHandler;
    private final Gson gson;

    public HistoryHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
        this.endpointHandler = new EndpointHandler()
                .handle(RequestMethod.GET, "/history", this::getHistory);

        this.gson = GsonFactory.taskGson();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        endpointHandler.handle(exchange);
    }

    private void getHistory(HttpExchange exchange) throws IOException {
        String historyJson = gson.toJson(taskManager.getHistory());
        endpointHandler.writeResponse(exchange, historyJson, SC_OK);
    }
}
