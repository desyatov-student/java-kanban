package main.java.ru.praktikum.kanban.model.dto.create;

public class CreateSubtaskDto extends BaseCreateTask {

    int epicId;

    public int getEpicId() {
        return epicId;
    }

    public CreateSubtaskDto(String name, String description, int epicId) {
        super(name, description);
        this.epicId = epicId;
    }
}
