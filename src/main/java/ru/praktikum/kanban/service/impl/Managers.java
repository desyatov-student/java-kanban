package ru.praktikum.kanban.service.impl;

import ru.praktikum.kanban.service.HistoryManager;
import ru.praktikum.kanban.service.TaskManager;
import ru.praktikum.kanban.service.backup.TaskFileStorage;
import ru.praktikum.kanban.repository.impl.FileBackedTaskRepository;

public class Managers {

    public static TaskManager getDefault() {
        FileBackedTaskRepository repository = new FileBackedTaskRepository(TaskFileStorage.defaultStorage());
        HistoryManager historyManager = new HistoryManagerImpl(repository);
        return new FileBackedTaskManager(repository, historyManager);
    }

}
