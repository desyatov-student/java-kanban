package ru.praktikum.kanban.service.impl;

import ru.praktikum.kanban.repository.impl.TaskRepositoryInMemory;
import ru.praktikum.kanban.service.TaskManager;
import ru.praktikum.kanban.util.IdentifierGenerator;
import ru.praktikum.kanban.util.MappingUtils;

public class Managers {

    public TaskManager getDefault() {
        return new InMemoryTaskManager(
                new IdentifierGenerator(),
                new TaskRepositoryInMemory(),
                new InMemoryHistoryManager<>(),
                new MappingUtils()
        );
    }
}
