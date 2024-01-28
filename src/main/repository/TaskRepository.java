package main.repository;

import java.util.ArrayList;
import java.util.HashMap;
import main.models.dto.TaskDto;
import main.models.entity.EpicEntity;
import main.models.entity.SubtaskEntity;
import main.models.entity.TaskEntity;

public class TaskRepository {
    private HashMap<Integer, TaskEntity> tasks;
    private HashMap<Integer, EpicEntity> epics;
    private HashMap<Integer, SubtaskEntity> subtasks;


    public ArrayList<TaskEntity> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<EpicEntity> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    public ArrayList<SubtaskEntity> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }
}
