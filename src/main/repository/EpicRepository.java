package main.repository;

import java.util.List;
import main.model.entity.EpicEntity;
import main.model.entity.SubtaskEntity;

public interface EpicRepository {

    List<EpicEntity> getAllEpics();
    EpicEntity getEpic(int epicId);
    EpicEntity getEpicWithSubtaskId(int subtaskId);
    void saveEpic(EpicEntity epicEntity);
    void saveEpic(EpicEntity epicEntity, List<SubtaskEntity> subtaskEntities);
    void removeEpic(int epicId);
    void removeAllEpics();
}

