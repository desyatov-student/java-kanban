package ru.praktikum.kanban.repository.impl;

import ru.praktikum.kanban.exception.TaskFileStorageException;
import ru.praktikum.kanban.model.TasksContainer;
import ru.praktikum.kanban.model.backed.file.TasksBackup;
import ru.praktikum.kanban.model.entity.BaseTaskEntity;
import ru.praktikum.kanban.model.entity.EpicEntity;
import ru.praktikum.kanban.model.entity.SubtaskEntity;
import ru.praktikum.kanban.model.entity.TaskEntity;
import ru.praktikum.kanban.util.Logger;

public class TaskRepositoryBackedFile extends TaskRepositoryInMemory {

    private final TaskFileStorage fileStorage;
    boolean isLoaded = false;
    private final Logger logger = Logger.getLogger(TaskRepositoryBackedFile.class);

    public TaskRepositoryBackedFile(TaskFileStorage fileStorage) {
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
        } catch (TaskFileStorageException e) {
            logger.error("Enable to save tasks changes", e);
        }
        isLoaded = true;
    }

    @Override
    public void saveTask(TaskEntity simpleTaskEntity) {
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
    public void saveEpic(EpicEntity epicEntity) {
        super.saveEpic(epicEntity);
        save();
    }

    @Override
    public void saveSubtask(SubtaskEntity subtaskEntity) {
        super.saveSubtask(subtaskEntity);
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
    public void addToHistory(BaseTaskEntity task) {
        super.addToHistory(task);
        save();
    }
}
