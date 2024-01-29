package main.repository;

import java.util.List;
import main.models.entity.SubtaskEntity;

public interface SubtaskRepository {

    public List<SubtaskEntity> getAllSubtasks();
    public List<SubtaskEntity> getSubtasksWithEpicId(int epicId);

}
