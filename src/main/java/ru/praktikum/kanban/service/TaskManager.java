package ru.praktikum.kanban.service;

import java.util.List;
import java.util.Optional;
import ru.praktikum.kanban.dto.CreateEpicDto;
import ru.praktikum.kanban.dto.CreateSubtaskDto;
import ru.praktikum.kanban.dto.CreateTaskDto;
import ru.praktikum.kanban.dto.TaskDto;
import ru.praktikum.kanban.dto.EpicDto;
import ru.praktikum.kanban.dto.SubtaskDto;
import ru.praktikum.kanban.dto.UpdateEpicDto;
import ru.praktikum.kanban.dto.UpdateSubtaskDto;
import ru.praktikum.kanban.dto.UpdateTaskDto;
import ru.praktikum.kanban.exception.TaskValidationException;

public interface TaskManager {

    // Task's methods
    List<TaskDto> getAllTasks();

    TaskDto createTask(CreateTaskDto createTaskDto) throws TaskValidationException;

    TaskDto updateTask(UpdateTaskDto updateTaskDto) throws TaskValidationException;

    Optional<TaskDto> getTask(Integer taskId);

    void removeTask(Integer taskId);

    void removeAllTasks();

    // Epic's methods

    List<EpicDto> getAllEpics();

    Optional<EpicDto> getEpic(Integer epicId);

    EpicDto createEpic(CreateEpicDto epicDto);

    EpicDto updateEpic(UpdateEpicDto updateEpicDto);

    void removeEpic(Integer epicId);

    void removeAllEpics();

    // Subtask's methods

    List<SubtaskDto> getAllSubtasks();

    Optional<SubtaskDto> getSubtask(Integer subtaskId);

    List<SubtaskDto> getSubtasksWithEpicId(Integer epicId);

    SubtaskDto createSubtask(CreateSubtaskDto subtaskDto) throws TaskValidationException;

    SubtaskDto updateSubtask(UpdateSubtaskDto updateSubtaskDto) throws TaskValidationException;

    void removeSubtask(Integer subtaskId);

    void removeAllSubtasks();

    // History's methods

    List<TaskDto> getHistory();

    List<TaskDto> getPrioritizedTasks();
}
