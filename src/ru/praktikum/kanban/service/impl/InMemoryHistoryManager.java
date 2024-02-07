package ru.praktikum.kanban.service.impl;

import java.util.ArrayList;
import java.util.List;
import ru.praktikum.kanban.service.HistoryManager;

public class InMemoryHistoryManager<T> implements HistoryManager<T> {

    public static int DEFAULT_MAX_SIZE = 10;

    private final ArrayList<T> tasks;
    private final int maxSize;

    public InMemoryHistoryManager(int maxSize) {
        this.tasks = new ArrayList<>();
        this.maxSize = maxSize;
    }

    @Override
    public List<T> getHistory() {
        return tasks;
    }

    @Override
    public void add(T object) {
        if (tasks.size() == maxSize) {
            tasks.remove(0);
        }
        tasks.add(object);
    }
}
