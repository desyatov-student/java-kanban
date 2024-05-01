package ru.praktikum.kanban.service.http;

import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.praktikum.kanban.util.Logger;

import static org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;

public class EndpointHandler {

    @FunctionalInterface
    public interface RequestMethodHandler<H> {
        void handle(H h) throws Exception;
    }

    @FunctionalInterface
    public interface BiRequestMethodHandler<P, H> {
        void handle(P p, H h) throws Exception;
    }

    @Getter
    @AllArgsConstructor
    private static class EndpointData {
        private RequestMethod requestMethod;
        private String pattern;
        private Map<String, String> params;
    }

    private static final Logger logger = Logger.getLogger(EndpointHandler.class);
    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    private final HashMap<RequestMethod, HashSet<String>> patterns;
    private final HashMap<String, RequestMethodHandler<HttpExchange>> endpointHandlers;
    private final HashMap<String, BiRequestMethodHandler<Map<String, String>, HttpExchange>> biEndpointHandlers;

    public EndpointHandler() {
        this.patterns = new HashMap<>();
        this.endpointHandlers = new HashMap<>();
        this.biEndpointHandlers = new HashMap<>();

        for (RequestMethod method : RequestMethod.values()) {
            patterns.put(method, new HashSet<>());
        }
    }

    public EndpointHandler handle(
            RequestMethod requestMethod,
            String pattern,
            RequestMethodHandler<HttpExchange> handler
    ) {
        patterns.get(requestMethod).add(pattern);
        String handlerKey = createHandlerKey(requestMethod, pattern);
        endpointHandlers.put(handlerKey, handler);
        return this;
    }

    public EndpointHandler handle(
            RequestMethod requestMethod,
            String pattern,
            BiRequestMethodHandler<Map<String, String>, HttpExchange> handler
    ) {
        patterns.get(requestMethod).add(pattern);
        String handlerKey = createHandlerKey(requestMethod, pattern);
        biEndpointHandlers.put(handlerKey, handler);
        return this;
    }

    public void handle(HttpExchange exchange) throws IOException {
        Optional<EndpointData> endpointDataOpt = getEndpointData(exchange);
        if (endpointDataOpt.isEmpty()) {
            writeResponse(exchange, SC_NOT_FOUND);
            return;
        }
        EndpointData endpointData = endpointDataOpt.get();
        String handlerKey = createHandlerKey(endpointData.getRequestMethod(), endpointData.getPattern());
        RequestMethodHandler<HttpExchange> handler = endpointHandlers.get(handlerKey);
        BiRequestMethodHandler<Map<String, String>, HttpExchange> biHandler = biEndpointHandlers.get(handlerKey);
        if (biHandler == null && handler == null) {
            writeResponse(exchange, SC_NOT_FOUND);
            return;
        }
        try {
            if (handler != null) {
                handler.handle(exchange);
            } else {
                biHandler.handle(endpointData.getParams(), exchange);
            }
        } catch (Throwable e) {
            logger.error("Failed handle HttpExchange", e);
            writeResponse(exchange, SC_INTERNAL_SERVER_ERROR);
        }

    }

    private Optional<EndpointData> getEndpointData(HttpExchange exchange) {
        RequestMethod requestMethod = RequestMethod.valueOf(exchange.getRequestMethod());
        HashSet<String> patternsSet = patterns.get(requestMethod);
        String path = exchange.getRequestURI().getPath();
        EndpointData endpointData = null;
        for (String pattern : patternsSet) {
            UrlPathParser urlPathParser = new UrlPathParser(pattern);
            boolean isValidPath = urlPathParser.tryParse(path);
            if (isValidPath) {
                endpointData = new EndpointData(requestMethod, pattern, urlPathParser.getParams());
                break;
            }
        }
        return Optional.ofNullable(endpointData);
    }

    private String createHandlerKey(RequestMethod requestMethod, String pattern) {
        return requestMethod + " " + pattern;
    }

    public void writeResponse(HttpExchange exchange, int responseCode) throws IOException {
        writeResponse(exchange, null, responseCode);
    }

    public void writeResponse(HttpExchange exchange, String response, int responseCode) throws IOException {
        Optional<String> responseOpt = Optional.ofNullable(response);
        try (OutputStream os = exchange.getResponseBody()) {
            exchange.sendResponseHeaders(responseCode, 0);
            if (responseOpt.isPresent()) {
                os.write(responseOpt.get().getBytes(DEFAULT_CHARSET));
            }
        }
        exchange.close();
    }
}