package ru.praktikum.kanban.repository;

import java.util.List;
import ru.praktikum.kanban.model.Subtask;

public interface SubtaskRepository {

    List<Subtask> getAllSubtasks();

    Subtask getSubtask(Integer subtaskId);

    void saveSubtask(Subtask subtask);

    void removeSubtask(Integer subtaskId);

    void removeAllSubtasks();
}
