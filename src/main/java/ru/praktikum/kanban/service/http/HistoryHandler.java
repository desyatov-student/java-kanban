package ru.praktikum.kanban.service.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import ru.praktikum.kanban.service.TaskManager;

public class HistoryHandler implements HttpHandler {

    private TaskManager taskManager;

    public HistoryHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

    }
}
