package main.java.ru.praktikum.kanban.service.impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import main.java.ru.praktikum.kanban.model.entity.BaseTaskEntity;
import main.java.ru.praktikum.kanban.service.HistoryManager;

public class InMemoryHistoryManager implements HistoryManager {

    public static int DEFAULT_MAX_SIZE = 10;

    private final LinkedList<BaseTaskEntity> history;
    private final int maxSize;

    public InMemoryHistoryManager(int maxSize) {
        this.history = new LinkedList<>();
        this.maxSize = maxSize;
    }

    public InMemoryHistoryManager() {
        this(DEFAULT_MAX_SIZE);
    }

    @Override
    public List<BaseTaskEntity> getHistory() {
        return new ArrayList<>(history);
    }

    @Override
    public void add(BaseTaskEntity object) {
        if (object == null) {
            return;
        }
        if (history.size() == maxSize) {
            history.remove(0);
        }
        history.add(object);
    }
}
