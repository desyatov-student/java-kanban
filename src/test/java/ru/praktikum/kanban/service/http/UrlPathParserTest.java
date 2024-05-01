package ru.praktikum.kanban.service.http;

import java.util.Map;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UrlPathParserTest {

    @Test
    void tryParse_ReturnTrueAndParameterValue_PathHasParameter() {
        // Given
        String pattern = "/tasks/{id}";
        String path = "/tasks/1";
        UrlPathParser urlPathParser = new UrlPathParser(pattern);

        // When

        boolean result = urlPathParser.tryParse(path);

        // Then

        assertTrue(result);
        assertEquals(Map.of("id", "1"), urlPathParser.getParams());
    }

    @Test
    void tryParse_ReturnTrueAndParameterValue_PathHasParameterInTheMiddle() {
        // Given
        String pattern = "/epics/{id}/subtasks";
        String path = "/epics/1/subtasks";
        UrlPathParser urlPathParser = new UrlPathParser(pattern);

        // When

        boolean result = urlPathParser.tryParse(path);

        // Then

        assertTrue(result);
        assertEquals(Map.of("id", "1"), urlPathParser.getParams());
    }

    @Test
    void tryParse_ReturnTrueAndNotParameterValue_PathWithoutParameter() {
        // Given
        String pattern = "/tasks";
        String path = "/tasks";
        UrlPathParser urlPathParser = new UrlPathParser(pattern);

        // When

        boolean result = urlPathParser.tryParse(path);

        // Then

        assertTrue(result);
        assertTrue(urlPathParser.getParams().isEmpty());
    }

    @Test
    void tryParse_ReturnFalseAndNoParameterValue_PathsNotMatch() {
        // Given
        String pattern = "/epics/{id}/subtasks";
        String path = "/epics/1/comments";
        UrlPathParser urlPathParser = new UrlPathParser(pattern);

        // When

        boolean result = urlPathParser.tryParse(path);

        // Then

        assertFalse(result);
        assertTrue(urlPathParser.getParams().isEmpty());
    }
}