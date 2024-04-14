package ru.praktikum.kanban.service.impl;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import ru.praktikum.kanban.model.Subtask;

class EpicTimeCollector implements Collector<Subtask, EpicTime, EpicTime> {

    @Override
    public Supplier<EpicTime> supplier() {
        return EpicTime::new;
    }

    @Override
    public BiConsumer<EpicTime, Subtask> accumulator() {
        return (epicTime, subtask) -> {
            epicTime.setStartTime(minTime(epicTime.getStartTime(), subtask.getStartTime()));
            epicTime.setDuration(sumNullableDuration(epicTime.getDuration(), subtask.getDuration()));
            epicTime.setEndTime(maxTime(epicTime.getEndTime(), subtask.getEndTime()));
        };
    }

    @Override
    public BinaryOperator<EpicTime> combiner() {
        return (epicTime1, epicTime2) -> {
            EpicTime epicTime = new EpicTime();
            epicTime.setStartTime(minTime(epicTime1.getStartTime(), epicTime2.getStartTime()));
            epicTime.setDuration(sumNullableDuration(epicTime1.getDuration(), epicTime2.getDuration()));
            epicTime.setEndTime(maxTime(epicTime1.getEndTime(), epicTime2.getEndTime()));
            return epicTime;
        };
    }

    @Override
    public Function<EpicTime, EpicTime> finisher() {
        return Function.identity();
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Set.of(Characteristics.IDENTITY_FINISH);
    }

    private Duration sumNullableDuration(Duration nullable, Duration duration) {
        return (nullable == null) ? duration : nullable.plus(duration);
    }

    private LocalDateTime minTime(LocalDateTime nullable, LocalDateTime time) {
        if (nullable == null) {
            return time;
        } else if (time.isBefore(nullable)) {
            return time;
        }
        return nullable;
    }

    private LocalDateTime maxTime(LocalDateTime nullable, LocalDateTime time) {
        if (nullable == null) {
            return time;
        } else if (time.isAfter(nullable)) {
            return time;
        }
        return nullable;
    }
}
