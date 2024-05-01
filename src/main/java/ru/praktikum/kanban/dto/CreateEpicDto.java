package ru.praktikum.kanban.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class CreateEpicDto {
    @NonNull private final String name;
    @NonNull private final String description;
}
