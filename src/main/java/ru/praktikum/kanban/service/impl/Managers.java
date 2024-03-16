package ru.praktikum.kanban.service.impl;

import ru.praktikum.kanban.repository.impl.TaskRepositoryInMemory;
import ru.praktikum.kanban.service.HistoryManager;
import ru.praktikum.kanban.service.TaskManager;

public class Managers {

    public static TaskManager getDefault() {
        TaskRepositoryInMemory repository = new TaskRepositoryInMemory();
        HistoryManager historyManager = new HistoryManagerImpl(repository);
        return new TaskManagerImpl(repository, historyManager);
    }

}
