package main.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import main.models.entity.EpicEntity;
import main.models.entity.SubtaskEntity;
import main.models.entity.TaskEntity;

public class TaskRepositoryInMemory implements Repository {
    private final HashMap<Integer, TaskEntity> tasks;
    private final HashMap<Integer, EpicEntity> epics;
    private final HashMap<Integer, List<Integer>> epicToSubtasksMap; // Связка ID эпика и массив ID подзадач. Пример: 1 -> [2, 3]
    private final HashMap<Integer, Integer> subtaskToEpicMap; // Связка ID подзадачи с ID эпика. Примеры: 2 -> 1, 3 -> 1
    private final HashMap<Integer, SubtaskEntity> subtasks;

    public TaskRepositoryInMemory() {
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.epicToSubtasksMap = new HashMap<>();
        this.subtaskToEpicMap = new HashMap<>();
        this.subtasks = new HashMap<>();
    }

    public List<TaskEntity> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    public void saveTask(TaskEntity taskEntity) {
        tasks.put(taskEntity.getId(), taskEntity);
    }

    public List<EpicEntity> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    public EpicEntity getEpicWithId(int epicId) {
        return epics.get(epicId);
    }

    public EpicEntity getEpicWithSubtaskId(int subtaskId) {
        int epicId = subtaskToEpicMap.get(subtaskId);
        return getEpicWithId(epicId);
    }

    public void saveEpic(EpicEntity epicEntity) {
        saveEpic(epicEntity, new ArrayList<>());
    }

    public void saveEpic(EpicEntity epicEntity, List<SubtaskEntity> subtaskEntities) {
        // Сохранение эпика по id
        epics.put(epicEntity.getId(), epicEntity);
        if (subtaskEntities.isEmpty()) {
            return;
        }
        List<Integer> subtaskIds = subtaskEntities.stream().map(SubtaskEntity::getId).collect(Collectors.toList());
        // Связка id эпика и id's подзадач
        epicToSubtasksMap.put(epicEntity.getId(), subtaskIds);
        for (SubtaskEntity subtaskEntity : subtaskEntities) {
            // Для каждой подзадачи делаем связку id подзадачи и id эпика
            subtaskToEpicMap.put(subtaskEntity.getId(), epicEntity.getId());
            // Сохранение подзадачи по id
            subtasks.put(subtaskEntity.getId(), subtaskEntity);
        }
    }

    public void removeEpic(int epicId) {
        List<Integer> subtaskIds = epicToSubtasksMap.get(epicId);
        for (Integer subtaskId : subtaskIds) {
            subtaskToEpicMap.remove(subtaskId);
            subtasks.remove(subtaskId);
        }
        epicToSubtasksMap.remove(epicId);
        epics.remove(epicId);
    }

    // Subtask's methods

    public List<SubtaskEntity> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }
    public List<SubtaskEntity> getSubtasksWithEpicId(int epicId) {
        return epicToSubtasksMap.getOrDefault(epicId, new ArrayList<>()).stream().map(
                subtaskId -> subtasks.get(subtaskId)
        ).collect(Collectors.toList());
    }
    public void removeSubtask(int subtaskId) {
        subtasks.remove(subtaskId);
    }
}
