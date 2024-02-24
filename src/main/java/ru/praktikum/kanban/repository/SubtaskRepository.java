package ru.praktikum.kanban.repository;

import java.util.List;
import ru.praktikum.kanban.model.entity.SubtaskEntity;

public interface SubtaskRepository {

    List<SubtaskEntity> getAllSubtasks();

    SubtaskEntity getSubtask(int subtaskId);

    void saveSubtask(SubtaskEntity subtaskEntity);

    void removeSubtask(int subtaskId);

    void removeAllSubtasks();
}
