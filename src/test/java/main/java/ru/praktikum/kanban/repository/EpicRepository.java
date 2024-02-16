package main.java.ru.praktikum.kanban.repository;

import java.util.List;
import main.java.ru.praktikum.kanban.model.entity.EpicEntity;

public interface EpicRepository {

    List<EpicEntity> getAllEpics();
    EpicEntity getEpic(int epicId);
    void saveEpic(EpicEntity epicEntity);
    void removeEpic(EpicEntity epicEntity);
    void removeAllEpics();
}

