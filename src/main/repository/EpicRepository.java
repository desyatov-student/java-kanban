package main.repository;

import java.util.List;
import main.models.entity.EpicEntity;
import main.models.entity.SubtaskEntity;

public interface EpicRepository {

    List<EpicEntity> getAllEpics();
    EpicEntity getEpicWithId(int epicId);
    EpicEntity getEpicWithSubtaskId(int subtaskId);
    void saveEpic(EpicEntity epicEntity);
    void saveEpic(EpicEntity epicEntity, List<SubtaskEntity> subtaskEntities);

}

