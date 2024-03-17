package ru.praktikum.kanban.service.impl;

import java.util.List;
import ru.praktikum.kanban.model.entity.BaseTaskEntity;
import ru.praktikum.kanban.repository.HistoryRepository;
import ru.praktikum.kanban.service.HistoryManager;

public class HistoryManagerImpl implements HistoryManager {

    private final HistoryRepository repository;

    public HistoryManagerImpl(HistoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<BaseTaskEntity> getHistory() {
        return repository.getHistory();
    }

    @Override
    public void remove(int id) {
        repository.removeFromHistory(id);
    }

    @Override
    public void add(BaseTaskEntity object) {
        if (object == null) {
            return;
        }
        repository.addToHistory(object);
    }
}
