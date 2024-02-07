package ru.praktikum.kanban.service.impl;

import ru.praktikum.kanban.model.dto.response.TaskDto;
import ru.praktikum.kanban.repository.impl.TaskRepositoryInMemory;
import ru.praktikum.kanban.service.HistoryManager;
import ru.praktikum.kanban.service.TaskManager;
import ru.praktikum.kanban.util.IdentifierGenerator;
import ru.praktikum.kanban.util.MappingUtils;

public class Managers {

    static public TaskManager getDefault() {
        return new InMemoryTaskManager(
                new IdentifierGenerator(),
                new TaskRepositoryInMemory(),
                Managers.getDefaultHistory(),
                new MappingUtils()
        );
    }

    static public HistoryManager<TaskDto> getDefaultHistory() {
        return new InMemoryHistoryManager<>();
    }
}
