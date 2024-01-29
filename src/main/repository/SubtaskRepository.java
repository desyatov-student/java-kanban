package main.repository;

import java.util.List;
import main.models.entity.SubtaskEntity;

public interface SubtaskRepository {

    List<SubtaskEntity> getAllSubtasks();
    List<SubtaskEntity> getSubtasksWithEpicId(int epicId);
    void removeSubtask(int subtaskId);
}
