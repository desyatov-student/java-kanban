package ru.praktikum.kanban.repository;

import java.util.List;
import ru.praktikum.kanban.model.Subtask;

public interface SubtaskRepository {

    List<Subtask> getAllSubtasks();

    Subtask getSubtask(int subtaskId);

    void saveSubtask(Subtask subtask);

    void removeSubtask(int subtaskId);

    void removeAllSubtasks();
}
