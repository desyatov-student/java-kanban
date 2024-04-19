package ru.praktikum.kanban.service.http;

import com.google.gson.Gson;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.praktikum.kanban.helper.RequestBuilder;
import ru.praktikum.kanban.helper.Response;
import ru.praktikum.kanban.helper.UriBuilder;
import ru.praktikum.kanban.service.TaskManager;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class ApiHandlerTest {

    @Mock
    TaskManager taskManager;

    HttpTaskServer httpServer;

    @BeforeEach
    void beforeEach() {
        httpServer = new HttpTaskServer(taskManager);
        httpServer.start();
    }

    @AfterEach
    void afterEach() {
        httpServer.stop(0);
    }

    RequestBuilder requestBuilder() {
        return new RequestBuilder();
    }
}