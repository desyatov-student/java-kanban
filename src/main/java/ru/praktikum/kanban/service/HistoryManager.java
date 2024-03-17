package ru.praktikum.kanban.service;

import java.util.List;
import ru.praktikum.kanban.model.entity.Task;

public interface HistoryManager {

    List<Task> getHistory();

    void remove(int id);

    void add(Task object);

}
