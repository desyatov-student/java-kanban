package ru.praktikum.kanban.repository.impl;

import ru.praktikum.kanban.exception.TaskFileStorageException;
import ru.praktikum.kanban.service.backup.TaskFileStorage;
import ru.praktikum.kanban.service.backup.TasksContainer;
import ru.praktikum.kanban.service.backup.TasksBackup;
import ru.praktikum.kanban.model.Task;
import ru.praktikum.kanban.model.Epic;
import ru.praktikum.kanban.model.Subtask;
import ru.praktikum.kanban.util.Logger;

public class FileBackedTaskRepository extends InMemoryTaskRepository {

    private final TaskFileStorage fileStorage;
    private boolean isLoaded = false;
    private final Logger logger = Logger.getLogger(FileBackedTaskRepository.class);

    public FileBackedTaskRepository(TaskFileStorage fileStorage) {
        this.fileStorage = fileStorage;
        loadFromFileStorage();
    }

    private void save() {
        TasksContainer tasksContainer = new TasksContainer(epics, subtasks, tasks);
        TasksBackup backup = new TasksBackup(tasksContainer, history.values());
        try {
            fileStorage.save(backup);
            logger.info("Success saved tasks changes: " + backup);
        } catch (TaskFileStorageException e) {
            logger.error("Enable to save tasks changes", e);
        }
    }

    private void loadFromFileStorage() {
        if (isLoaded) {
            return;
        }
        try {
            TasksBackup backup = fileStorage.getBackup();
            TasksContainer tasksContainer = backup.getTasksContainer();
            epics.clear();
            epics.putAll(tasksContainer.epics);

            subtasks.clear();
            subtasks.putAll(tasksContainer.subtasks);

            tasks.clear();
            tasks.putAll(tasksContainer.tasks);

            history.clear();
            history.putAll(backup.getHistory());

            logger.info("Success loaded tasks from file: " + backup);
            isLoaded = true;
        } catch (TaskFileStorageException e) {
            logger.error("Enable to load tasks from file", e);
        }
    }

    @Override
    public void saveTask(Task simpleTaskEntity) {
        super.saveTask(simpleTaskEntity);
        save();
    }

    @Override
    public void removeTask(Integer taskId) {
        super.removeTask(taskId);
        save();
    }

    @Override
    public void removeAllTasks() {
        super.removeAllTasks();
        save();
    }

    @Override
    public void saveEpic(Epic epic) {
        super.saveEpic(epic);
        save();
    }

    @Override
    public void saveSubtask(Subtask subtask) {
        super.saveSubtask(subtask);
        save();
    }

    @Override
    public void removeEpic(Integer epicId) {
        super.removeEpic(epicId);
        save();
    }

    @Override
    public void removeAllEpics() {
        super.removeAllEpics();
        save();
    }

    @Override
    public void removeSubtask(Integer subtaskId) {
        super.removeSubtask(subtaskId);
        save();
    }

    @Override
    public void removeAllSubtasks() {
        super.removeAllSubtasks();
        save();
    }

    @Override
    public void removeFromHistory(Integer id) {
        super.removeFromHistory(id);
        save();
    }

    @Override
    public void addToHistory(Task task) {
        super.addToHistory(task);
        save();
    }
}
