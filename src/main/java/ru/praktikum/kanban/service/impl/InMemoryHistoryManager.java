package ru.praktikum.kanban.service.impl;

import java.util.ArrayList;
import java.util.List;
import ru.praktikum.kanban.model.HistoryLinkedList;
import ru.praktikum.kanban.model.dto.response.BaseTaskDto;
import ru.praktikum.kanban.service.HistoryManager;

public class InMemoryHistoryManager implements HistoryManager {

    private final HistoryLinkedList history;

    public InMemoryHistoryManager() {
        this(new HistoryLinkedList());
    }

    public InMemoryHistoryManager(HistoryLinkedList history) {
        this.history = history;
    }

    @Override
    public List<BaseTaskDto> getHistory() {
        return new ArrayList<>(history.values());
    }

    @Override
    public void remove(int id) {
        history.remove(id);
    }

    @Override
    public void add(BaseTaskDto object) {
        if (object == null) {
            return;
        }
        history.add(object);
    }
}
