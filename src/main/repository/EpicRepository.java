package main.repository;

import java.util.List;
import main.model.entity.EpicEntity;
import main.model.entity.SubtaskEntity;

public interface EpicRepository {

    List<EpicEntity> getAllEpics();
    EpicEntity getEpic(int epicId);
    void saveEpic(EpicEntity epicEntity);
    void removeEpic(EpicEntity epicEntity);
    void removeAllEpics();
}

