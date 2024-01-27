package main.models;

public class Subtask extends Task {

    private Epic epic;

    public void setEpic(Epic epic) {
        this.epic = epic;
    }

    public Subtask(int id, String name, String description, TaskStatus status) {
        super(id, name, description, status);
    }
}
