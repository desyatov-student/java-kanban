package ru.praktikum.kanban.helper;

import java.util.List;
import ru.praktikum.kanban.dto.EpicDto;
import ru.praktikum.kanban.dto.SubtaskDto;
import ru.praktikum.kanban.dto.TaskDto;
import ru.praktikum.kanban.model.TaskStatus;
import ru.praktikum.kanban.model.Epic;
import ru.praktikum.kanban.model.Subtask;
import ru.praktikum.kanban.model.Task;

public final class TaskFactory {

    public static String DEFAULT_NAME = "Name";
    public static String DEFAULT_DESCRIPTION = "Description";

    private TaskFactory() {
    }

    public static Subtask SUBTASK(Integer id) { return SUBTASK(id, TaskStatus.NEW, 0); }
    public static Subtask SUBTASK(Integer id, Integer epicId) { return SUBTASK(id, TaskStatus.NEW, epicId); }
    public static Subtask SUBTASK(Integer id, TaskStatus status) { return SUBTASK(id,status, 0); }
    public static Subtask SUBTASK(Integer id, TaskStatus status, Integer epicId) { return new Subtask(id, DEFAULT_NAME, DEFAULT_DESCRIPTION, status, epicId, null, null); }

    public static Epic EPIC(Integer id) { return EPIC(id, TaskStatus.NEW, List.of()); }
    public static Epic EPIC(Integer id, List<Integer> subtasks) { return EPIC(id, TaskStatus.NEW, subtasks); }
    public static Epic EPIC(Integer id, TaskStatus status) { return EPIC(id, status, List.of()); }
    public static Epic EPIC(Integer id, TaskStatus status, List<Integer> subtasks) { return new Epic(id, DEFAULT_NAME, DEFAULT_DESCRIPTION, status, subtasks, null, null); }

    public static Task TASK(Integer id) { return TASK(id, TaskStatus.NEW); }
    public static Task TASK(Integer id, String name) { return new Task(id, name, DEFAULT_DESCRIPTION, TaskStatus.NEW, null, null); }
    public static Task TASK(Integer id, TaskStatus status) { return new Task(id, DEFAULT_NAME, DEFAULT_DESCRIPTION, status, null, null); }

    // DTO:

    public static EpicDto EPIC_DTO(Integer id) { return EPIC_DTO(id, TaskStatus.NEW); }
    public static EpicDto EPIC_DTO(Integer id, TaskStatus status) { return EPIC_DTO(id, status, List.of()); }
    public static EpicDto EPIC_DTO(Integer id, String name, String description) { return new EpicDto(id, name == null ? DEFAULT_NAME : name, description == null ? DEFAULT_DESCRIPTION : description, TaskStatus.NEW, List.of(), null, null, null); }
    public static EpicDto EPIC_DTO(Integer id, List<SubtaskDto> subtasks) { return EPIC_DTO(id, TaskStatus.NEW, subtasks); }
    public static EpicDto EPIC_DTO(Integer id, TaskStatus status, List<SubtaskDto> subtasks) { return new EpicDto(id, DEFAULT_NAME, DEFAULT_DESCRIPTION, status, subtasks, null, null, null); }

    public static SubtaskDto SUBTASK_DTO(Integer id) { return  SUBTASK_DTO(id, TaskStatus.NEW); }
    public static SubtaskDto SUBTASK_DTO(Integer id, TaskStatus status) { return new SubtaskDto(id, DEFAULT_NAME, DEFAULT_DESCRIPTION, status, null, null, null); }

    public static TaskDto TASK_DTO(Integer id) { return TASK_DTO(id, TaskStatus.NEW); }
    public static TaskDto TASK_DTO(Integer id, TaskStatus status) { return new TaskDto(id, DEFAULT_NAME, DEFAULT_DESCRIPTION, status, null, null, null); }


}
