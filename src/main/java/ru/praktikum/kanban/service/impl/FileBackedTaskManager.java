package ru.praktikum.kanban.service.impl;

import ru.praktikum.kanban.repository.TaskManagerRepository;
import ru.praktikum.kanban.service.HistoryManager;
import ru.praktikum.kanban.util.IdentifierGenerator;

public class FileBackedTaskManager extends InMemoryTaskManager {
    public FileBackedTaskManager(IdentifierGenerator identifierGenerator, TaskManagerRepository repository, HistoryManager historyManager) {
        super(identifierGenerator, repository, historyManager);
    }

    public FileBackedTaskManager(TaskManagerRepository repository, HistoryManager historyManager) {
        super(repository, historyManager);
    }
}
