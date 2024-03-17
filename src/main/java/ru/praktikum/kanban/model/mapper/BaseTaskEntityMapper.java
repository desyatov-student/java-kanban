package ru.praktikum.kanban.model.mapper;

import java.util.HashMap;
import java.util.function.Function;
import ru.praktikum.kanban.model.TaskType;
import ru.praktikum.kanban.model.entity.Epic;
import ru.praktikum.kanban.model.entity.Task;
import ru.praktikum.kanban.model.entity.Subtask;
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

    AbstractMapper<Task, String> entityToStringMapper;
    HashMap<TaskType, Function<Input, Task>> inputToEntityMapper;
    TaskMapper taskMapper;
    SubtaskMapper subtaskMapper;
    EpicMapper epicMapper;

    public BaseTaskEntityMapper() {
        this.entityToStringMapper = new AbstractMapper<>();
        this.inputToEntityMapper = new HashMap<>();
        this.taskMapper = new TaskMapperImpl();
        this.subtaskMapper = new SubtaskMapperImpl();
        this.epicMapper = new EpicMapperImpl();

        entityToStringMapper.put(Task.class, task -> taskMapper.toString((Task) task));
        entityToStringMapper.put(Subtask.class, task -> subtaskMapper.toString((Subtask) task));
        entityToStringMapper.put(Epic.class, task -> epicMapper.toString((Epic) task));

        inputToEntityMapper.put(TaskType.TASK, input -> {
            Task task = taskMapper.toEntity(input.propertiesValues);
            input.tasksContainer.tasks.put(task.getId(), task);
            return task;
        });
        inputToEntityMapper.put(TaskType.SUBTASK, input -> {
            Subtask subtask = subtaskMapper.toEntity(input.propertiesValues);
            Epic epic = input.tasksContainer.epics.get(subtask.getEpicId());
            epic.subtasks.add(subtask.getEpicId());
            input.tasksContainer.subtasks.put(subtask.getId(), subtask);
            return subtask;
        });
        inputToEntityMapper.put(TaskType.EPIC, input -> {
            Epic epic = epicMapper.toEntity(input.propertiesValues);
            input.tasksContainer.epics.put(epic.getId(), epic);
            return epic;
        });
    }

    public String toString(Task task) {
        return entityToStringMapper.tryMap(task);
    }

    public Task toModel(Input input) {
        return inputToEntityMapper.get(input.taskType).apply(input);
    }
}

