package main.repository;

import java.util.List;
import main.models.entity.EpicEntity;
import main.models.entity.SubtaskEntity;

public interface EpicRepository {

    public List<EpicEntity> getAllEpics();
    public EpicEntity getEpicWithId(int epicId);
    public EpicEntity getEpicWithSubtaskId(int subtaskId);
    public void saveEpic(EpicEntity epicEntity);
    public void saveEpic(EpicEntity epicEntity, List<SubtaskEntity> subtaskEntities);

}

