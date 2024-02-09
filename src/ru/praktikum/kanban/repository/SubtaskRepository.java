package ru.praktikum.kanban.repository;

import java.util.List;
import ru.praktikum.kanban.model.entity.SubtaskEntityBase;

public interface SubtaskRepository {

    List<SubtaskEntityBase> getAllSubtasks();
    SubtaskEntityBase getSubtask(int subtaskId);
    void saveSubtask(SubtaskEntityBase subtaskEntity);
    void removeSubtask(int subtaskId);
    void removeAllSubtasks();
}
