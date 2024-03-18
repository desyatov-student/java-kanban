package ru.praktikum.kanban.repository.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import ru.praktikum.kanban.service.impl.HistoryLinkedList;
import ru.praktikum.kanban.model.Epic;
import ru.praktikum.kanban.model.Subtask;
import ru.praktikum.kanban.model.Task;
import ru.praktikum.kanban.repository.HistoryRepository;
import ru.praktikum.kanban.repository.TaskManagerRepository;

public class InMemoryTaskRepository implements TaskManagerRepository, HistoryRepository {
    protected final HashMap<Integer, Task> tasks;
    protected final HashMap<Integer, Epic> epics;
    protected final HashMap<Integer, Subtask> subtasks;
    protected final HistoryLinkedList history;

    public InMemoryTaskRepository() {
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subtasks = new HashMap<>();
        this.history = new HistoryLinkedList();
    }

    // Task's methods

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values())
                .stream()
                .sorted(Comparator.comparing(Task::getId))
                .collect(Collectors.toList());
    }

    @Override
    public void saveTask(Task task) {
        tasks.put(task.getId(), task);
    }

    @Override
    public Task getTask(int taskId) {
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
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values())
                .stream()
                .sorted(Comparator.comparing(Epic::getId))
                .collect(Collectors.toList());
    }

    @Override
    public Epic getEpic(int epicId) {
        return epics.get(epicId);
    }

    @Override
    public void saveEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    @Override
    public void saveSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
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
    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values())
                .stream()
                .sorted(Comparator.comparing(Subtask::getId))
                .collect(Collectors.toList());
    }

    @Override
    public Subtask getSubtask(int subtaskId) {
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
    public List<Task> getHistory() {
        return new ArrayList<>(history.values());
    }

    @Override
    public void removeFromHistory(int id) {
        history.remove(id);
    }

    @Override
    public void addToHistory(Task task) {
        if (task == null) {
            return;
        }
        history.add(task);
    }
}
