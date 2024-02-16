package main.java.ru.praktikum.kanban.service;

import java.util.List;
import main.java.ru.praktikum.kanban.model.entity.BaseTaskEntity;

public interface HistoryManager {

    List<BaseTaskEntity> getHistory();

    void add(BaseTaskEntity object);

}
