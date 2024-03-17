package ru.praktikum.kanban.model.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import ru.praktikum.kanban.model.TasksContainer;
import ru.praktikum.kanban.model.entity.EpicEntity;
import ru.praktikum.kanban.model.entity.SubtaskEntity;
import ru.praktikum.kanban.model.entity.TaskEntity;

public final class CollectionsHelper {
    private CollectionsHelper() {
    }

    public static TasksContainer tasksListsToContainer(
            List<EpicEntity> epics,
            List<SubtaskEntity> subtasks,
            List<TaskEntity> tasks
    ) {
        Map<Integer, EpicEntity> epicMap = epics.stream()
                .collect(Collectors.toMap(EpicEntity::getId, Function.identity()));
        Map<Integer, SubtaskEntity> subtasksMap = subtasks.stream()
                .collect(Collectors.toMap(SubtaskEntity::getId, Function.identity()));
        Map<Integer, TaskEntity> tasksMap = tasks.stream()
                .collect(Collectors.toMap(TaskEntity::getId, Function.identity()));

        return new TasksContainer(new HashMap<>(epicMap), new HashMap<>(subtasksMap), new HashMap<>(tasksMap));
    }
}
