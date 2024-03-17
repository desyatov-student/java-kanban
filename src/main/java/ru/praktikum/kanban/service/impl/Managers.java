package ru.praktikum.kanban.service.impl;

import ru.praktikum.kanban.repository.impl.TaskFileStorage;
import ru.praktikum.kanban.repository.impl.FileBackedTaskRepository;
import ru.praktikum.kanban.service.HistoryManager;
import ru.praktikum.kanban.service.TaskManager;

public class Managers {

    public static TaskManager getDefault() {
        FileBackedTaskRepository repository = new FileBackedTaskRepository(TaskFileStorage.defaultStorage());
        repository.loadFromFileStorage();
        HistoryManager historyManager = new HistoryManagerImpl(repository);
        return new TaskManagerImpl(repository, historyManager);
    }

}
