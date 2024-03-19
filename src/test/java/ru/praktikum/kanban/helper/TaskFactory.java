package ru.praktikum.kanban.helper;

import java.util.List;
import ru.praktikum.kanban.model.TaskStatus;
import ru.praktikum.kanban.model.Epic;
import ru.praktikum.kanban.model.Subtask;
import ru.praktikum.kanban.model.Task;

public final class TaskFactory {

    private TaskFactory() {
    }

    public static Subtask SUBTASK(Integer id) { return SUBTASK(id, TaskStatus.NEW, 0); }
    public static Subtask SUBTASK(Integer id, Integer epicId) { return SUBTASK(id, TaskStatus.NEW, epicId); }
    public static Subtask SUBTASK(Integer id, TaskStatus status) { return SUBTASK(id,status, 0); }
    public static Subtask SUBTASK(Integer id, TaskStatus status, Integer epicId) { return new Subtask(id, "Name", "Description", status, epicId); }

    public static Epic EPIC(Integer id) { return EPIC(id, TaskStatus.NEW, List.of()); }
    public static Epic EPIC(Integer id, List<Integer> subtasks) { return EPIC(id, TaskStatus.NEW, subtasks); }
    public static Epic EPIC(Integer id, TaskStatus status) { return EPIC(id, status, List.of()); }
    public static Epic EPIC(Integer id, TaskStatus status, List<Integer> subtasks) { return new Epic(id, "Name", "Description", status, subtasks); }
//    CreateEpicDto CREATE_EPIC() { return new CreateEpicDto("name", "desc"); }

    public static Task TASK(Integer id) { return TASK(id, TaskStatus.NEW); }
    public static Task TASK(Integer id, TaskStatus status) { return new Task(id, "Name", "Description", status); }
}
