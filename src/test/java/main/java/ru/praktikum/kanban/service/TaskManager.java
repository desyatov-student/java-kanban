package main.java.ru.praktikum.kanban.service;

import java.util.List;
import main.java.ru.praktikum.kanban.model.dto.create.CreateEpicDto;
import main.java.ru.praktikum.kanban.model.dto.create.CreateSubtaskDto;
import main.java.ru.praktikum.kanban.model.dto.create.CreateTaskDto;
import main.java.ru.praktikum.kanban.model.dto.response.BaseTaskDto;
import main.java.ru.praktikum.kanban.model.dto.response.EpicDto;
import main.java.ru.praktikum.kanban.model.dto.response.SubtaskDto;
import main.java.ru.praktikum.kanban.model.dto.response.TaskDto;
import main.java.ru.praktikum.kanban.model.dto.update.UpdateEpicDto;
import main.java.ru.praktikum.kanban.model.dto.update.UpdateSubtaskDto;
import main.java.ru.praktikum.kanban.model.dto.update.UpdateTaskDto;

public interface TaskManager {

    // Task's methods
    List<TaskDto> getAllTasks();

    TaskDto createTask(CreateTaskDto createTaskDto);

    TaskDto updateTask(UpdateTaskDto updateTaskDto);

    TaskDto getTask(int taskId);

    void removeTask(int taskId);

    void removeAllTasks();

    // Epic's methods

    List<EpicDto> getAllEpics();

    EpicDto getEpic(int epicId);

    EpicDto createEpic(CreateEpicDto epicDto);

    EpicDto updateEpic(UpdateEpicDto updateEpicDto);

    void removeEpic(int epicId);

    void removeAllEpics();

    // Subtask's methods

    List<SubtaskDto> getAllSubtasks();

    SubtaskDto getSubtask(int subtaskId);

    List<SubtaskDto> getSubtasksWithEpicId(int epicId);

    SubtaskDto createSubtask(CreateSubtaskDto subtaskDto);

    SubtaskDto updateSubtask(UpdateSubtaskDto updateSubtaskDto);

    void removeSubtask(int subtaskId);

    void removeAllSubtasks();

    // History's methods

    List<BaseTaskDto> getHistory();
}
