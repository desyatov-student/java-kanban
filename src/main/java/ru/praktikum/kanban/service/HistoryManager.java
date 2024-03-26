package ru.praktikum.kanban.service;

import java.util.List;
import ru.praktikum.kanban.model.Task;

public interface HistoryManager {

    List<Task> getHistory();

    void remove(Integer id);

    void add(Task object);

}
