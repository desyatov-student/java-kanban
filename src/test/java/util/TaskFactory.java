package util;

import java.util.List;
import ru.praktikum.kanban.model.TaskStatus;
import ru.praktikum.kanban.model.entity.EpicEntity;
import ru.praktikum.kanban.model.entity.SubtaskEntity;
import ru.praktikum.kanban.model.entity.TaskEntity;

public final class TaskFactory {

    private TaskFactory() {
    }

    public static SubtaskEntity SUBTASK(int id) { return SUBTASK(id, TaskStatus.NEW, 0); }
    public static SubtaskEntity SUBTASK(int id, int epicId) { return SUBTASK(id, TaskStatus.NEW, epicId); }
    public static SubtaskEntity SUBTASK(int id, TaskStatus status) { return SUBTASK(id,status, 0); }
    public static SubtaskEntity SUBTASK(int id, TaskStatus status, int epicId) { return new SubtaskEntity(id, "Name", "Description", status, epicId); }

    public static EpicEntity EPIC(int id) { return EPIC(id, TaskStatus.NEW, List.of()); }
    public static EpicEntity EPIC(int id, List<Integer> subtasks) { return EPIC(id, TaskStatus.NEW, subtasks); }
    public static EpicEntity EPIC(int id, TaskStatus status) { return EPIC(id, status, List.of()); }
    public static EpicEntity EPIC(int id, TaskStatus status, List<Integer> subtasks) { return new EpicEntity(id, "Name", "Description", status, subtasks); }

    public static TaskEntity TASK(int id) { return TASK(id, TaskStatus.NEW); }
    public static TaskEntity TASK(int id, TaskStatus status) { return new TaskEntity(id, "Name", "Description", status); }
}
