package ru.praktikum.kanban.util;

import ru.praktikum.kanban.config.ApplicationConfig;

public class Logger {

    ApplicationConfig config = new ApplicationConfig();

    enum Level {
        ERROR,
        INFO,
    }

    String className;

    public static Logger getLogger(Class clazz) {
        String className = clazz.getSimpleName();
        return new Logger(className);
    }

    public Logger(String className) {
        this.className = className;
    }

    public void info(String message) {
        print(Level.INFO, message, null);
    }

    public void info(String message, Throwable exception) {
        print(Level.INFO, message, exception);
    }

    public void error(String message) {
        error(message, null);
    }

    public void error(String message, Throwable exception) {
        print(Level.ERROR, message, exception);
    }

    private void print(Level level, String message, Throwable exception) {
        if (!config.isLogEnabled()) {
            return;
        }
        String coloredLevel;
        String coloredMessage;
        switch (level) {
            case ERROR:
                coloredLevel = "[" + red(level.toString()) + "]";
                coloredMessage = red(message);
                break;
            case INFO:
            default:
                coloredLevel = "[" + blue(level.toString()) + "]";
                coloredMessage = message;
        }
        String format = String.format("%s %s %s", coloredLevel, className, coloredMessage);
        if (exception != null) {
            format = format + " cause: " + exception;
        }
        System.out.println(format);
        if (level == Level.ERROR && exception != null) {
            exception.printStackTrace();
        }
    }

    private String green(String value) {
        return "\u001B[32m" + value + "\u001B[0m";
    }

    private String red(String value) {
        return "\u001B[31m" + value + "\u001B[0m";
    }

    private String blue(String value) {
        return "\u001B[34m" + value + "\u001B[0m";
    }
}
