package ru.praktikum.kanban.repository;

import java.util.List;
import ru.praktikum.kanban.model.Epic;

public interface EpicRepository {

    List<Epic> getAllEpics();

    Epic getEpic(Integer epicId);

    void saveEpic(Epic epic);

    void removeEpic(Integer epicId);

    void removeAllEpics();
}

