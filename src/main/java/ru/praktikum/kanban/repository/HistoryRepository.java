package ru.praktikum.kanban.repository;

import java.util.List;
import ru.praktikum.kanban.model.Task;

public interface HistoryRepository {
    List<Task> getHistory();

    void removeFromHistory(Integer id);

    void addToHistory(Task task);
}
