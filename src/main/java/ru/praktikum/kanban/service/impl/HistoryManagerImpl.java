package ru.praktikum.kanban.service.impl;

import java.util.List;
import ru.praktikum.kanban.model.Task;
import ru.praktikum.kanban.repository.HistoryRepository;
import ru.praktikum.kanban.service.HistoryManager;

public class HistoryManagerImpl implements HistoryManager {

    private final HistoryRepository repository;

    public HistoryManagerImpl(HistoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Task> getHistory() {
        return repository.getHistory();
    }

    @Override
    public void remove(int id) {
        repository.removeFromHistory(id);
    }

    @Override
    public void add(Task object) {
        if (object == null) {
            return;
        }
        repository.addToHistory(object);
    }
}
