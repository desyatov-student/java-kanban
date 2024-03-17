package ru.praktikum.kanban.repository.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import ru.praktikum.kanban.model.HistoryLinkedList;
import ru.praktikum.kanban.model.entity.BaseTaskEntity;
import ru.praktikum.kanban.model.entity.EpicEntity;
import ru.praktikum.kanban.model.entity.SubtaskEntity;
import ru.praktikum.kanban.model.entity.TaskEntity;
import ru.praktikum.kanban.repository.HistoryRepository;
import ru.praktikum.kanban.repository.TaskManagerRepository;

public class InMemoryTaskRepository implements TaskManagerRepository, HistoryRepository {
    protected final HashMap<Integer, TaskEntity> tasks;
    protected final HashMap<Integer, EpicEntity> epics;
    protected final HashMap<Integer, SubtaskEntity> subtasks;
    protected final HistoryLinkedList history;

    public InMemoryTaskRepository() {
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subtasks = new HashMap<>();
        this.history = new HistoryLinkedList();
    }

    // Task's methods

    @Override
    public List<TaskEntity> getAllTasks() {
        return new ArrayList<>(tasks.values())
                .stream()
                .sorted(Comparator.comparing(TaskEntity::getId))
                .collect(Collectors.toList());
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
        return new ArrayList<>(epics.values())
                .stream()
                .sorted(Comparator.comparing(EpicEntity::getId))
                .collect(Collectors.toList());
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
        return new ArrayList<>(subtasks.values())
                .stream()
                .sorted(Comparator.comparing(SubtaskEntity::getId))
                .collect(Collectors.toList());
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

    // History

    @Override
    public List<BaseTaskEntity> getHistory() {
        return new ArrayList<>(history.values());
    }

    @Override
    public void removeFromHistory(int id) {
        history.remove(id);
    }

    @Override
    public void addToHistory(BaseTaskEntity task) {
        if (task == null) {
            return;
        }
        history.add(task);
    }
}
