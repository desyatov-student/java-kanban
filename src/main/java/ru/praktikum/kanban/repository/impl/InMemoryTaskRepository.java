package ru.praktikum.kanban.repository.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;
import ru.praktikum.kanban.model.Epic;
import ru.praktikum.kanban.model.Subtask;
import ru.praktikum.kanban.model.Task;
import ru.praktikum.kanban.repository.HistoryRepository;
import ru.praktikum.kanban.repository.TaskManagerRepository;
import ru.praktikum.kanban.service.impl.HistoryLinkedList;

public class InMemoryTaskRepository implements TaskManagerRepository, HistoryRepository {
    protected final HashMap<Integer, Task> tasks;
    protected final HashMap<Integer, Epic> epics;
    protected final HashMap<Integer, Subtask> subtasks;
    protected final HistoryLinkedList history;
    protected final TreeSet<Task> prioritizedTasks;

    public InMemoryTaskRepository() {
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subtasks = new HashMap<>();
        this.history = new HistoryLinkedList();
        this.prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));
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
        if (task.getStartTime() != null) {
            prioritizedTasks.add(task);
        }
    }

    @Override
    public Task getTask(Integer taskId) {
        return tasks.get(taskId);
    }

    @Override
    public void removeTask(Integer taskId) {
        Task task = tasks.remove(taskId);
        if (validateStartTime(task)) {
            prioritizedTasks.remove(task);
        }
    }

    @Override
    public void removeAllTasks() {
        tasks.values().stream()
                .filter(this::validateStartTime)
                .forEach(prioritizedTasks::remove);
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
    public Epic getEpic(Integer epicId) {
        return epics.get(epicId);
    }

    @Override
    public void saveEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    @Override
    public void removeEpic(Integer epicId) {
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
    public Subtask getSubtask(Integer subtaskId) {
        return subtasks.get(subtaskId);
    }

    @Override
    public void saveSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        if (subtask.getStartTime() != null) {
            prioritizedTasks.add(subtask);
        }
    }

    @Override
    public void removeSubtask(Integer subtaskId) {
        Subtask subtask = subtasks.remove(subtaskId);
        if (validateStartTime(subtask)) {
            prioritizedTasks.remove(subtask);
        }
    }

    @Override
    public void removeAllSubtasks() {
        subtasks.values().stream()
                .filter(this::validateStartTime)
                .forEach(prioritizedTasks::remove);
        subtasks.clear();
    }

    // History

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(history.values());
    }

    @Override
    public void removeFromHistory(Integer id) {
        history.remove(id);
    }

    @Override
    public void addToHistory(Task task) {
        if (task == null) {
            return;
        }
        history.add(task);
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    private boolean validateStartTime(Task task) {
        return task != null && task.getStartTime() != null;
    }
}
