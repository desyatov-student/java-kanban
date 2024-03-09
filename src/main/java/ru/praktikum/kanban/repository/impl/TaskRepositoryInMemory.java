package ru.praktikum.kanban.repository.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import ru.praktikum.kanban.model.entity.EpicEntity;
import ru.praktikum.kanban.model.entity.SubtaskEntity;
import ru.praktikum.kanban.model.entity.TaskEntity;
import ru.praktikum.kanban.repository.Repository;

public class TaskRepositoryInMemory implements Repository {
    private final HashMap<Integer, TaskEntity> tasks;
    private final HashMap<Integer, EpicEntity> epics;
    private final HashMap<Integer, SubtaskEntity> subtasks;

    public TaskRepositoryInMemory() {
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subtasks = new HashMap<>();
    }

    // Task's methods

    @Override
    public List<TaskEntity> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public void saveTask(TaskEntity simpleTaskEntity) {
        tasks.put(simpleTaskEntity.getId(), simpleTaskEntity);
    }

    @Override
    public TaskEntity getTask(int taskId) {
        return tasks.get(taskId);
    }

    @Override
    public void removeTask(int taskId) {
        tasks.remove(taskId);
    }

    @Override
    public void removeAllTasks() {
        tasks.clear();
    }

    // Epic's methods

    @Override
    public List<EpicEntity> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public EpicEntity getEpic(int epicId) {
        return epics.get(epicId);
    }

    @Override
    public void saveEpic(EpicEntity epicEntity) {
        epics.put(epicEntity.getId(), epicEntity);
    }

    @Override
    public void saveSubtask(SubtaskEntity subtaskEntity) {
        subtasks.put(subtaskEntity.getId(), subtaskEntity);
    }

    @Override
    public void removeEpic(int epicId) {
        epics.remove(epicId);
    }

    @Override
    public void removeAllEpics() {
        epics.clear();
    }

    // Subtask's methods

    @Override
    public List<SubtaskEntity> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public SubtaskEntity getSubtask(int subtaskId) {
        return subtasks.get(subtaskId);
    }

    @Override
    public void removeSubtask(int subtaskId) {
        subtasks.remove(subtaskId);
    }

    @Override
    public void removeAllSubtasks() {
        subtasks.clear();
    }
}
