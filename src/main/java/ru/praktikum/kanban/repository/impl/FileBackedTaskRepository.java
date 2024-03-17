package ru.praktikum.kanban.repository.impl;

import ru.praktikum.kanban.exception.TaskFileStorageException;
import ru.praktikum.kanban.model.TasksContainer;
import ru.praktikum.kanban.model.backed.file.TasksBackup;
import ru.praktikum.kanban.model.entity.Task;
import ru.praktikum.kanban.model.entity.Epic;
import ru.praktikum.kanban.model.entity.Subtask;
import ru.praktikum.kanban.util.Logger;

public class FileBackedTaskRepository extends InMemoryTaskRepository {

    private final TaskFileStorage fileStorage;
    boolean isLoaded = false;
    private final Logger logger = Logger.getLogger(FileBackedTaskRepository.class);

    public FileBackedTaskRepository(TaskFileStorage fileStorage) {
        this.fileStorage = fileStorage;
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

    public void loadFromFileStorage() {
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
            logger.error("Enable to save tasks changes", e);
        }
    }

    @Override
    public void saveTask(Task simpleTaskEntity) {
        super.saveTask(simpleTaskEntity);
        save();
    }

    @Override
    public void removeTask(int taskId) {
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
    public void removeEpic(int epicId) {
        super.removeEpic(epicId);
        save();
    }

    @Override
    public void removeAllEpics() {
        super.removeAllEpics();
        save();
    }

    @Override
    public void removeSubtask(int subtaskId) {
        super.removeSubtask(subtaskId);
        save();
    }

    @Override
    public void removeAllSubtasks() {
        super.removeAllSubtasks();
        save();
    }

    @Override
    public void removeFromHistory(int id) {
        super.removeFromHistory(id);
        save();
    }

    @Override
    public void addToHistory(Task task) {
        super.addToHistory(task);
        save();
    }
}
