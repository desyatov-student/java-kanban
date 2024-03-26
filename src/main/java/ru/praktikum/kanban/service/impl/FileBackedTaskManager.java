package ru.praktikum.kanban.service.impl;

import ru.praktikum.kanban.repository.TaskManagerRepository;
import ru.praktikum.kanban.service.HistoryManager;

public class FileBackedTaskManager extends InMemoryTaskManager {

    public FileBackedTaskManager(TaskManagerRepository repository, HistoryManager historyManager) {
        super(repository, historyManager);
    }
}
