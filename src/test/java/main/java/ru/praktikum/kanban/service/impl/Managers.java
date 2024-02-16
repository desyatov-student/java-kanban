package main.java.ru.praktikum.kanban.service.impl;

import main.java.ru.praktikum.kanban.service.HistoryManager;
import main.java.ru.praktikum.kanban.service.TaskManager;

public class Managers {

    static public TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    static public HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
