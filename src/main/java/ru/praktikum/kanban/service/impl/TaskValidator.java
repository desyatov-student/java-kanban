package ru.praktikum.kanban.service.impl;

import java.util.List;
import ru.praktikum.kanban.model.Task;

public class TaskValidator {

    public boolean hasIntersectionOfTime(Task task, List<Task> tasks) {
        return tasks.stream()
                .anyMatch(task2 -> hasIntersectionOfTime(task, task2));
    }

    public boolean hasIntersectionOfTime(Task task1, Task task2) {
        // start2 end2 start1 end1
        // 8-9 9-10 or 8-9 10-11
        if (task2.getEndTime().isBefore(task1.getStartTime()) || task2.getEndTime().isEqual(task1.getStartTime())) {
            return false;
        }
        // start1 end1 start2 end2
        // 9-10 10-11 or 9-10 11-12
        if (task1.getEndTime().isBefore(task2.getStartTime()) || task1.getEndTime().isEqual(task2.getStartTime())) {
            return false;
        }
        return true;
    }
}
