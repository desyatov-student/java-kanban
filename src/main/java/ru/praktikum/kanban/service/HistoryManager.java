package ru.praktikum.kanban.service;

import java.util.List;
import ru.praktikum.kanban.model.entity.BaseTaskEntity;

public interface HistoryManager {

    List<BaseTaskEntity> getHistory();

    void add(BaseTaskEntity object);

}
