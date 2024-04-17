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

    Optional<TaskDto> updateTask(Integer id, UpdateTaskDto updateTaskDto) throws TaskValidationException;

    Optional<TaskDto> getTask(Integer id);

    void removeTask(Integer id);

    void removeAllTasks();

    // Epic's methods

    List<EpicDto> getAllEpics();

    Optional<EpicDto> getEpic(Integer id);

    EpicDto createEpic(CreateEpicDto epicDto);

    Optional<EpicDto> updateEpic(Integer id, UpdateEpicDto updateEpicDto);

    void removeEpic(Integer id);

    void removeAllEpics();

    // Subtask's methods

    List<SubtaskDto> getAllSubtasks();

    Optional<SubtaskDto> getSubtask(Integer id);

    List<SubtaskDto> getSubtasksWithEpicId(Integer id);

    SubtaskDto createSubtask(CreateSubtaskDto subtaskDto) throws TaskValidationException;

    SubtaskDto updateSubtask(Integer id, UpdateSubtaskDto updateSubtaskDto) throws TaskValidationException;

    void removeSubtask(Integer id);

    void removeAllSubtasks();

    // History's methods

    List<TaskDto> getHistory();

    List<TaskDto> getPrioritizedTasks();
}
