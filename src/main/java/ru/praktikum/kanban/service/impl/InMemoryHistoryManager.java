package ru.praktikum.kanban.service.impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import ru.praktikum.kanban.model.HistoryLinkedList;
import ru.praktikum.kanban.model.entity.BaseTaskEntity;
import ru.praktikum.kanban.service.HistoryManager;

public class InMemoryHistoryManager implements HistoryManager {

    private final HistoryLinkedList history;

    public InMemoryHistoryManager() {
        this.history = new HistoryLinkedList();
    }

    @Override
    public List<BaseTaskEntity> getHistory() {
        return new ArrayList<>(history.values());
    }

    @Override
    public void remove(int id) {
        history.remove(id);
    }

    @Override
    public void add(BaseTaskEntity object) {
        if (object == null) {
            return;
        }
        history.add(object);
    }
}
