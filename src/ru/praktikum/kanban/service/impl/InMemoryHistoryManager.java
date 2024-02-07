package ru.praktikum.kanban.service.impl;

import java.util.ArrayList;
import java.util.List;
import ru.praktikum.kanban.service.HistoryManager;

public class InMemoryHistoryManager<T> implements HistoryManager<T> {

    static int MAX_NUMBER_OF_ELEMENTS = 10;

    private final ArrayList<T> tasks;

    public InMemoryHistoryManager() {
        this.tasks = new ArrayList<>();
    }

    @Override
    public List<T> getHistory() {
        return tasks;
    }

    @Override
    public void addTask(T object) {
        if (tasks.size() == MAX_NUMBER_OF_ELEMENTS) {
            tasks.remove(0);
        }
        tasks.add(object);
    }
}
