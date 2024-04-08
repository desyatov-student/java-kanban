package ru.praktikum.kanban.repository;

import java.util.List;
import java.util.Optional;
import ru.praktikum.kanban.model.Task;

public interface TaskManagerRepository extends TaskRepository, SubtaskRepository, EpicRepository {

    Optional<Integer> getLastId();

    List<Task> getPrioritizedTasks();

}
