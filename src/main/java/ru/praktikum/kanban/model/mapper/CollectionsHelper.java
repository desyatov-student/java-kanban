package ru.praktikum.kanban.model.mapper;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import ru.praktikum.kanban.model.TaskType;
import ru.praktikum.kanban.model.entity.BaseTaskEntity;
import ru.praktikum.kanban.model.entity.EpicEntity;
import ru.praktikum.kanban.model.entity.SubtaskEntity;
import ru.praktikum.kanban.model.entity.TaskEntity;

public final class CollectionsHelper {
    private CollectionsHelper() {
    }

    public static List<BaseTaskEntity> mapToList(HashMap<TaskType, HashMap<Integer, BaseTaskEntity>> tasks) {
        return Arrays.stream(TaskType.values())
                .map(type -> tasks.getOrDefault(type, new HashMap<>()).values())
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public static HashMap<TaskType, HashMap<Integer, BaseTaskEntity>> listsToMap(List<EpicEntity> epics,
                                  List<SubtaskEntity> subtasks,
                                  List<TaskEntity> tasks) {
        Map<Integer, BaseTaskEntity> epicMap = epics.stream()
                .collect(Collectors.toMap(EpicEntity::getId, Function.identity()));
        Map<Integer, BaseTaskEntity> subtasksMap = subtasks.stream()
                .collect(Collectors.toMap(SubtaskEntity::getId, Function.identity()));
        Map<Integer, BaseTaskEntity> tasksMap = tasks.stream()
                .collect(Collectors.toMap(TaskEntity::getId, Function.identity()));

        HashMap<TaskType, HashMap<Integer, BaseTaskEntity>> resultMap = new HashMap<>();
        resultMap.put(
                TaskType.EPIC,
                new HashMap<>(epicMap)
        );
        resultMap.put(
                TaskType.SUBTASK,
                new HashMap<>(subtasksMap)
        );
        resultMap.put(
                TaskType.TASK,
                new HashMap<>(tasksMap)
        );
        return resultMap;
    }
}
