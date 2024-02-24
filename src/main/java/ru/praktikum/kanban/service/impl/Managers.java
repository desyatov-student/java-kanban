package ru.praktikum.kanban.service.impl;

import ru.praktikum.kanban.service.HistoryManager;
import ru.praktikum.kanban.service.TaskManager;

public class Managers {

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
