package ru.praktikum.kanban.util;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ru.praktikum.kanban.constant.DelimiterConstants.DELIMITER_COMMA;

public final class StringUtils {

    private StringUtils() {
    }

    public static String joining(String delimiter, Object ... args) {
        return Stream.of(args)
                .map(String::valueOf)
                .collect(Collectors.joining(DELIMITER_COMMA));
    }
}
