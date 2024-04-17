package ru.praktikum.kanban.service.http;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import ru.praktikum.kanban.service.TaskManager;
import ru.praktikum.kanban.service.impl.Managers;
import ru.praktikum.kanban.util.Logger;

public class HttpTaskServer {

    private static final int PORT = 8080;
    private static final Logger logger = Logger.getLogger(HttpTaskServer.class);
    private final HttpServer httpServer;
    private final TaskManager taskManager;

    public HttpTaskServer(TaskManager taskManager) {
        this.taskManager = taskManager;
        this.httpServer = createHttpServer();
    }

    public void start() {
        httpServer.start();
    }

    public void stop(int delay) {
        httpServer.stop(delay);
    }

    private HttpServer createHttpServer() {
        try {
            HttpServer httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
            httpServer.createContext("/pics", new EpicsHandler(taskManager));
            httpServer.createContext("/subtask", new SubtasksHandler(taskManager));
            httpServer.createContext("/tasks", new TasksHandler(taskManager));
            httpServer.createContext("/history", new HistoryHandler(taskManager));
            httpServer.createContext("/prioritized", new PrioritizedHandler(taskManager));
            return httpServer;
        } catch (IOException e) {
            logger.error("Failed to create HttpServer", e);
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        HttpTaskServer httpTaskServer = new HttpTaskServer(Managers.getDefault());
        httpTaskServer.start();
        logger.info("HttpTaskServer has started");
    }
}
