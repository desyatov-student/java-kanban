package ru.praktikum.kanban.util;

public class Logger {

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
        System.out.println(format(Level.INFO, message, null));
    }

    public void error(String message) {
        error(message, null);
    }

    public void error(String message, Throwable exception) {
        System.out.println(format(Level.ERROR, message, exception));
    }

    private String format(Level level, String message, Throwable exception) {
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
            return  format + " cause: " + exception;
        }
        return format;
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
