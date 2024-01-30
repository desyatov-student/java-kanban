package main.repository;

import java.util.List;
import main.model.entity.SubtaskEntity;

public interface SubtaskRepository {

    List<SubtaskEntity> getAllSubtasks();
    SubtaskEntity getSubtask(int subtaskId);
    List<SubtaskEntity> getSubtasksWithEpicId(int epicId);
    void removeSubtask(int subtaskId);
    public void removeAllSubtasks();
}
