package ru.praktikum.kanban.model.dto.update;

public class UpdateEpicDto extends BaseUpdateTask {

    public UpdateEpicDto(int id, String name, String description) {
        super(id, name, description);
    }
}
