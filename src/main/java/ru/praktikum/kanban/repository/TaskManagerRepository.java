package ru.praktikum.kanban.repository;

import java.util.List;
import ru.praktikum.kanban.model.Task;

public interface TaskManagerRepository extends TaskRepository, SubtaskRepository, EpicRepository {

    List<Task> getPrioritizedTasks();

}
