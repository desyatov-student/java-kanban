package ru.praktikum.kanban;

import java.time.Duration;
import java.time.LocalDateTime;
import ru.praktikum.kanban.dto.CreateEpicDto;
import ru.praktikum.kanban.dto.CreateSubtaskDto;
import ru.praktikum.kanban.dto.CreateTaskDto;
import ru.praktikum.kanban.dto.EpicDto;
import ru.praktikum.kanban.dto.SubtaskDto;
import ru.praktikum.kanban.dto.TaskDto;
import ru.praktikum.kanban.exception.TaskValidationException;
import ru.praktikum.kanban.service.TaskManager;
import ru.praktikum.kanban.service.impl.Managers;

public class Main {

    public static void main(String[] args) throws TaskValidationException {

        TaskManager taskManager = Managers.getDefault();
        LocalDateTime startTime = LocalDateTime.now();
        Duration duration = Duration.ofMinutes(10);

        final TaskDto task1 = taskManager.createTask(
                new CreateTaskDto("Таск 1", "Description", startTime, duration)
        );

        startTime = startTime.plusHours(1);
        final TaskDto task2 = taskManager.createTask(
                new CreateTaskDto("Таск 2", "Description", startTime, duration)
        );

        final EpicDto epic = taskManager.createEpic(
                new CreateEpicDto("Эпик с подзадачами", "Description")
        );

        final EpicDto emptyEpic = taskManager.createEpic(
                new CreateEpicDto("Пустой эпик", "Description")
        );

        startTime = startTime.plusHours(1);
        final SubtaskDto subtask1 = taskManager.createSubtask(
                new CreateSubtaskDto(
                        "Проектирование",
                        "Description", epic.getId(), startTime, duration)
        );

        startTime = startTime.plusHours(1);
        final SubtaskDto subtask2 = taskManager.createSubtask(
                new CreateSubtaskDto(
                        "Тест 1",
                        "Description", epic.getId(), startTime, duration)
        );

        startTime = startTime.plusHours(1);
        final SubtaskDto subtask3 = taskManager.createSubtask(
                new CreateSubtaskDto(
                        "Тест 2",
                        "Description", epic.getId(), startTime, duration)
        );

        startTime = startTime.plusHours(1);
        final SubtaskDto subtask4 = taskManager.createSubtask(
                new CreateSubtaskDto(
                        "Код",
                        "Description", epic.getId(), startTime, duration)
        );

        taskManager.getEpic(epic.getId());
        System.out.println(taskManager.getHistory());
        taskManager.getEpic(emptyEpic.getId());
        System.out.println(taskManager.getHistory());
        taskManager.getTask(task1.getId());
        System.out.println(taskManager.getHistory());
        taskManager.getTask(task2.getId());
        System.out.println(taskManager.getHistory());
        taskManager.getTask(subtask1.getId());
        System.out.println(taskManager.getHistory());
        taskManager.getTask(task1.getId());
        System.out.println(taskManager.getHistory());
        taskManager.getEpic(epic.getId());
        System.out.println(taskManager.getHistory());

        System.out.println();
        System.out.println("1️⃣ ****** Данные в первом менеджере: ******");
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllSubtasks());
        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getHistory());

        System.out.println();
        TaskManager taskManager2 = Managers.getDefault();
        System.out.println();
        System.out.println("2️⃣ ****** Данные во втором менеджере: ******");
        System.out.println(taskManager2.getAllEpics());
        System.out.println(taskManager2.getAllSubtasks());
        System.out.println(taskManager2.getAllTasks());
        System.out.println(taskManager2.getHistory());

    }
}
