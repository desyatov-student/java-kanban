package ru.praktikum.kanban.service.impl;

import ru.praktikum.kanban.repository.impl.TaskFileStorage;
import ru.praktikum.kanban.repository.impl.TaskRepositoryBackedFile;
import ru.praktikum.kanban.service.HistoryManager;
import ru.praktikum.kanban.service.TaskManager;

public class Managers {

    public static TaskManager getDefault() {
        TaskRepositoryBackedFile repository = new TaskRepositoryBackedFile(TaskFileStorage.defaultStorage());
        repository.loadFromFileStorage();
        HistoryManager historyManager = new HistoryManagerImpl(repository);
        return new TaskManagerImpl(repository, historyManager);
    }

}
