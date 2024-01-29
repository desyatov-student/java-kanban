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
    private final HashMap<Integer, List<Integer>> epicToSubtasksMap;
    private final HashMap<Integer, Integer> subtaskToEpicMap;
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
        epics.put(epicEntity.getId(), epicEntity);
        if (subtaskEntities.isEmpty()) {
            return;
        }
        List<Integer> subtaskIds = subtaskEntities.stream().map(SubtaskEntity::getId).collect(Collectors.toList());
        epicToSubtasksMap.put(epicEntity.getId(), subtaskIds);
        for (SubtaskEntity subtaskEntity : subtaskEntities) {
            subtaskToEpicMap.put(subtaskEntity.getId(), epicEntity.getId());
            subtasks.put(subtaskEntity.getId(), subtaskEntity);
        }
    }

    public List<SubtaskEntity> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    public List<SubtaskEntity> getSubtasksWithEpicId(int epicId) {
        return epicToSubtasksMap.getOrDefault(epicId, new ArrayList<>()).stream().map(
                subtaskId -> subtasks.get(subtaskId)
        ).collect(Collectors.toList());
    }
}
