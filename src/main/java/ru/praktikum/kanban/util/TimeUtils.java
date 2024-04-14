package ru.praktikum.kanban.util;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import ru.praktikum.kanban.service.backup.TaskFileStorage;

public final class TimeUtils {

    private static final Logger logger = Logger.getLogger(TaskFileStorage.class);

    public static Optional<LocalDateTime> parseDateTime(String value) {
        LocalDateTime result = null;
        try {
            result = LocalDateTime.parse(value);
        } catch (Exception e) {
            logger.error("Enable to parse LocalDateTime from string: " + value, e);
        }
        return Optional.ofNullable(result);
    }

    public static Optional<Duration> parseDuration(String value) {
        Duration result = null;
        try {
            result = Duration.ofMinutes(Long.parseLong(value));
        } catch (Exception e) {
            logger.error("Enable to parse Duration from string: " + value, e);
        }
        return Optional.ofNullable(result);
    }
}
