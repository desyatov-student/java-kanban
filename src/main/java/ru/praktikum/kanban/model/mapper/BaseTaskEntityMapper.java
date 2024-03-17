package ru.praktikum.kanban.model.mapper;

import java.util.HashMap;
import java.util.function.Function;
import ru.praktikum.kanban.model.TaskType;
import ru.praktikum.kanban.model.entity.BaseTaskEntity;
import ru.praktikum.kanban.model.entity.EpicEntity;
import ru.praktikum.kanban.model.entity.SubtaskEntity;
import ru.praktikum.kanban.model.entity.TaskEntity;
import ru.praktikum.kanban.model.TasksContainer;
import ru.praktikum.kanban.util.AbstractMapper;

public class BaseTaskEntityMapper {

    public static class Input {
        String[] propertiesValues;
        TaskType taskType;
        TasksContainer tasksContainer;

        public Input(String[] propertiesValues, TaskType taskType, TasksContainer tasksContainer) {
            this.propertiesValues = propertiesValues;
            this.taskType = taskType;
            this.tasksContainer = tasksContainer;
        }
    }

    AbstractMapper<BaseTaskEntity, String> entityToStringMapper;
    HashMap<TaskType, Function<Input, BaseTaskEntity>> inputToEntityMapper;
    TaskMapper taskMapper;
    SubtaskMapper subtaskMapper;
    EpicMapper epicMapper;

    public BaseTaskEntityMapper() {
        this.entityToStringMapper = new AbstractMapper<>();
        this.inputToEntityMapper = new HashMap<>();
        this.taskMapper = new TaskMapperImpl();
        this.subtaskMapper = new SubtaskMapperImpl();
        this.epicMapper = new EpicMapperImpl();

        entityToStringMapper.put(TaskEntity.class, task -> taskMapper.toString((TaskEntity) task));
        entityToStringMapper.put(SubtaskEntity.class, task -> subtaskMapper.toString((SubtaskEntity) task));
        entityToStringMapper.put(EpicEntity.class, task -> epicMapper.toString((EpicEntity) task));

        inputToEntityMapper.put(TaskType.TASK, input -> {
            TaskEntity task = taskMapper.toEntity(input.propertiesValues);
            input.tasksContainer.tasks.put(task.getId(), task);
            return task;
        });
        inputToEntityMapper.put(TaskType.SUBTASK, input -> {
            SubtaskEntity subtask = subtaskMapper.toEntity(input.propertiesValues);
            EpicEntity epic = input.tasksContainer.epics.get(subtask.getEpicId());
            epic.subtasks.add(subtask.getEpicId());
            input.tasksContainer.subtasks.put(subtask.getId(), subtask);
            return subtask;
        });
        inputToEntityMapper.put(TaskType.EPIC, input -> {
            EpicEntity epic = epicMapper.toEntity(input.propertiesValues);
            input.tasksContainer.epics.put(epic.getId(), epic);
            return epic;
        });
    }

    public String toString(BaseTaskEntity task) {
        return entityToStringMapper.tryMap(task);
    }

    public BaseTaskEntity toModel(Input input) {
        return inputToEntityMapper.get(input.taskType).apply(input);
    }
}

