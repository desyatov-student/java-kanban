package ru.praktikum.kanban.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
@EqualsAndHashCode(of = {"id"})
public class Task {
    @Setter(AccessLevel.NONE)
    @NonNull final Integer id;
    @NonNull private String name;
    @NonNull private String description;
    @NonNull private TaskStatus status;
}
