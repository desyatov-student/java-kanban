package ru.praktikum.kanban.service;

import java.util.List;
import ru.praktikum.kanban.model.dto.create.CreateEpicDto;
import ru.praktikum.kanban.model.dto.create.CreateSubtaskDto;
import ru.praktikum.kanban.model.dto.create.CreateSimpleTaskDto;
import ru.praktikum.kanban.model.dto.response.TaskDto;
import ru.praktikum.kanban.model.dto.response.EpicDto;
import ru.praktikum.kanban.model.dto.response.SubtaskDto;
import ru.praktikum.kanban.model.dto.response.SimpleTaskDto;
import ru.praktikum.kanban.model.dto.update.UpdateEpicDto;
import ru.praktikum.kanban.model.dto.update.UpdateSubtaskDto;
import ru.praktikum.kanban.model.dto.update.UpdateTaskDto;

public interface TaskManager {

    // Task's methods
    List<SimpleTaskDto> getAllTasks();

    SimpleTaskDto createTask(CreateSimpleTaskDto createSimpleTaskDto);

    SimpleTaskDto updateTask(UpdateTaskDto updateTaskDto);

    SimpleTaskDto getTask(int taskId);

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

    SubtaskDto createSubtask(CreateSubtaskDto subtaskDto, int epicId);

    SubtaskDto updateSubtask(UpdateSubtaskDto updateSubtaskDto);

    void removeSubtask(int subtaskId);

    void removeAllSubtasks();

    // History's methods

    List<TaskDto> getHistory();
}
