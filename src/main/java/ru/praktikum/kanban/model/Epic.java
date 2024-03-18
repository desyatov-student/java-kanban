package ru.praktikum.kanban.model;

import java.util.ArrayList;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Epic extends Task {

    @EqualsAndHashCode.Exclude
    public final List<Integer> subtasks;

    public Epic(
            int id,
            @NonNull String name,
            @NonNull String description,
            @NonNull TaskStatus status,
            @NonNull List<Integer> subtasks) {
        super(id, name, description, status);
        this.subtasks = new ArrayList<>(subtasks);
    }
}
