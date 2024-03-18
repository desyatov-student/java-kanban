package ru.praktikum.kanban.service.mapper;

import java.util.HashMap;
import java.util.function.Function;
import ru.praktikum.kanban.model.TaskType;
import ru.praktikum.kanban.model.entity.Epic;
import ru.praktikum.kanban.model.entity.Subtask;
import ru.praktikum.kanban.model.entity.Task;
import ru.praktikum.kanban.service.backup.TasksContainer;
import ru.praktikum.kanban.util.AbstractMapper;

public class AdvancedTaskMapper {

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

    AbstractMapper<Task, String> taskToStringMapper;
    HashMap<TaskType, Function<Input, Task>> valuesToTaskMapper;
    TaskMapper taskMapper;
    SubtaskMapper subtaskMapper;
    EpicMapper epicMapper;

    public AdvancedTaskMapper() {
        this.taskToStringMapper = new AbstractMapper<>();
        this.valuesToTaskMapper = new HashMap<>();
        this.taskMapper = new TaskMapperImpl();
        this.subtaskMapper = new SubtaskMapperImpl();
        this.epicMapper = new EpicMapperImpl();

        taskToStringMapper.put(Task.class, task -> taskMapper.toString(task));
        taskToStringMapper.put(Subtask.class, task -> subtaskMapper.toString((Subtask) task));
        taskToStringMapper.put(Epic.class, task -> epicMapper.toString((Epic) task));

        valuesToTaskMapper.put(TaskType.TASK, input -> {
            Task task = taskMapper.toEntity(input.propertiesValues);
            input.tasksContainer.addTask(task);
            return task;
        });
        valuesToTaskMapper.put(TaskType.SUBTASK, input -> {
            Subtask subtask = subtaskMapper.toEntity(input.propertiesValues);
            Epic epic = input.tasksContainer.epics.get(subtask.getEpicId());
            epic.subtasks.add(subtask.getEpicId());
            input.tasksContainer.addSubtask(subtask);
            return subtask;
        });
        valuesToTaskMapper.put(TaskType.EPIC, input -> {
            Epic epic = epicMapper.toEntity(input.propertiesValues);
            input.tasksContainer.addEpic(epic);
            return epic;
        });
    }

    public String toString(Task task) {
        return taskToStringMapper.tryMap(task);
    }

    public Task toModel(Input input) {
        return valuesToTaskMapper.get(input.taskType).apply(input);
    }
}

