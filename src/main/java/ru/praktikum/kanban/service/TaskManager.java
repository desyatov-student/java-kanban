package ru.praktikum.kanban.service;

import java.util.List;
import ru.praktikum.kanban.model.CreateEpic;
import ru.praktikum.kanban.model.CreateSubtask;
import ru.praktikum.kanban.model.CreateTask;
import ru.praktikum.kanban.model.TaskDto;
import ru.praktikum.kanban.model.EpicDto;
import ru.praktikum.kanban.model.SubtaskDto;
import ru.praktikum.kanban.model.UpdateEpic;
import ru.praktikum.kanban.model.UpdateSubtask;
import ru.praktikum.kanban.model.UpdateTask;

public interface TaskManager {

    // Task's methods
    List<TaskDto> getAllTasks();

    TaskDto createTask(CreateTask createTask);

    TaskDto updateTask(UpdateTask updateTask);

    TaskDto getTask(int taskId);

    void removeTask(int taskId);

    void removeAllTasks();

    // Epic's methods

    List<EpicDto> getAllEpics();

    EpicDto getEpic(int epicId);

    EpicDto createEpic(CreateEpic epicDto);

    EpicDto updateEpic(UpdateEpic updateEpic);

    void removeEpic(int epicId);

    void removeAllEpics();

    // Subtask's methods

    List<SubtaskDto> getAllSubtasks();

    SubtaskDto getSubtask(int subtaskId);

    List<SubtaskDto> getSubtasksWithEpicId(int epicId);

    SubtaskDto createSubtask(CreateSubtask subtaskDto);

    SubtaskDto updateSubtask(UpdateSubtask updateSubtask);

    void removeSubtask(int subtaskId);

    void removeAllSubtasks();

    // History's methods

    List<TaskDto> getHistory();
}
