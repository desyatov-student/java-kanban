package ru.praktikum.kanban.service;

import java.util.List;
import ru.praktikum.kanban.model.dto.response.BaseTaskDto;

public interface HistoryManager {

    List<BaseTaskDto> getHistory();

    void remove(int id);

    void add(BaseTaskDto object);

}
