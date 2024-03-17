package ru.praktikum.kanban.repository;

import java.util.List;
import ru.praktikum.kanban.model.entity.Task;

public interface HistoryRepository {
    List<Task> getHistory();

    void removeFromHistory(int id);

    void addToHistory(Task task);
}
