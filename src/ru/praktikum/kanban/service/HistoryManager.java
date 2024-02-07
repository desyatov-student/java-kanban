package ru.praktikum.kanban.service;

import java.util.List;

public interface HistoryManager<T> {

    List<T> getHistory();

    void add(T object);

}
