package main.models.entity;

import java.util.List;
import main.models.BaseTask;

public class EpicEntity extends BaseTask {

    private List<Integer> subtasksIds;

    public EpicEntity(int id, String name, String description, List<Integer> subtasksIds) {
        super(id, name, description);
        this.subtasksIds = subtasksIds;
    }
}
