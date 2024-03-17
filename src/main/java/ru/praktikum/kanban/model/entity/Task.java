package ru.praktikum.kanban.model.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import ru.praktikum.kanban.model.TaskStatus;

@Getter
@Setter
@AllArgsConstructor
@ToString
@EqualsAndHashCode(of = {"id"})
public class Task {
    @Setter(AccessLevel.NONE)
    private final int id;
    @NonNull public String name;
    @NonNull public String description;
    @NonNull public TaskStatus status;
}
