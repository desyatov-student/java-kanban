package ru.praktikum.kanban.service.impl;

import ru.praktikum.kanban.repository.HistoryRepository;
import ru.praktikum.kanban.repository.impl.FileBackedTaskRepository;
import ru.praktikum.kanban.repository.impl.InMemoryTaskRepository;
import ru.praktikum.kanban.service.HistoryManager;
import ru.praktikum.kanban.service.TaskManager;
import ru.praktikum.kanban.service.backup.TaskFileStorage;

public class Managers {

    public static TaskManager getDefault() {
        FileBackedTaskRepository repository = new FileBackedTaskRepository(TaskFileStorage.defaultStorage());
        HistoryManager historyManager = getDefaultHistory(repository);
        return new FileBackedTaskManager(repository, historyManager);
    }

    public static TaskManager getInMemoryTaskManager() {
        InMemoryTaskRepository repository = new InMemoryTaskRepository();
        HistoryManager historyManager = getDefaultHistory(repository);
        return new InMemoryTaskManager(repository, historyManager);
    }

    public static HistoryManager getDefaultHistory(HistoryRepository repository) {
        return new HistoryManagerImpl(repository);
    }
}
