package ru.praktikum.kanban.repository;

import java.util.List;
import ru.praktikum.kanban.model.entity.BaseTaskEntity;

public interface HistoryRepository {
    List<BaseTaskEntity> getHistory();

    void remove(int id);

    void add(BaseTaskEntity task);
}
