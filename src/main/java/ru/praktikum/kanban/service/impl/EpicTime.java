package ru.praktikum.kanban.service.impl;

import java.time.Duration;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
class EpicTime {
    private LocalDateTime startTime;
    private Duration duration;
    private LocalDateTime endTime;
}
