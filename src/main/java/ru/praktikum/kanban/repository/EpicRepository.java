package ru.praktikum.kanban.repository;

import java.util.List;
import ru.praktikum.kanban.model.Epic;

public interface EpicRepository {

    List<Epic> getAllEpics();

    Epic getEpic(int epicId);

    void saveEpic(Epic epic);

    void removeEpic(int epicId);

    void removeAllEpics();
}

