package ru.praktikum.kanban.service.impl;

import ru.praktikum.kanban.model.entity.TaskEntity;
import ru.praktikum.kanban.repository.impl.TaskRepositoryInMemory;
import ru.praktikum.kanban.service.HistoryManager;
import ru.praktikum.kanban.service.TaskManager;
import ru.praktikum.kanban.util.IdentifierGenerator;

public class Managers {

    static public TaskManager getDefault() {
        return new InMemoryTaskManager(
                new IdentifierGenerator(),
                new TaskRepositoryInMemory(),
                Managers.getDefaultHistory()
        );
    }

    static public HistoryManager<TaskEntity> getDefaultHistory() {
        return new InMemoryHistoryManager<>(InMemoryHistoryManager.DEFAULT_MAX_SIZE);
    }
}
