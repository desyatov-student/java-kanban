package ru.praktikum.kanban;

import ru.praktikum.kanban.model.CreateEpic;
import ru.praktikum.kanban.model.CreateSubtask;
import ru.praktikum.kanban.model.CreateTask;
import ru.praktikum.kanban.model.EpicDto;
import ru.praktikum.kanban.model.SubtaskDto;
import ru.praktikum.kanban.model.TaskDto;
import ru.praktikum.kanban.service.TaskManager;
import ru.praktikum.kanban.service.impl.Managers;

public class Main {

    public static void main(String[] args) {

        TaskManager taskManager = Managers.getDefault();

        final TaskDto task1 = taskManager.createTask(
                new CreateTask("Таск 1", "Description")
        );

        final TaskDto task2 = taskManager.createTask(
                new CreateTask("Таск 2", "Description")
        );

        EpicDto epic = taskManager.createEpic(
                new CreateEpic("Эпик с подзадачами", "Description")
        );

        EpicDto emptyEpic = taskManager.createEpic(
                new CreateEpic("Пустой эпик", "Description")
        );

        final SubtaskDto subtask1 = taskManager.createSubtask(
                new CreateSubtask("Проектирование", "Спроектировать хранение данных", epic.getId())
        );
        SubtaskDto subtask2 = taskManager.createSubtask(
                new CreateSubtask("Тест", "Написать тесты заглушки", epic.getId())
        );
        SubtaskDto subtask3 = taskManager.createSubtask(
                new CreateSubtask("Тест", "Написать тесты заглушки", epic.getId())
        );
        SubtaskDto subtask4 = taskManager.createSubtask(
                new CreateSubtask("Код", "Написать код", epic.getId())
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

        System.out.println("Удаляем задачу...");
        taskManager.removeTask(task1.getId());

        System.out.println(taskManager.getHistory());

        System.out.println("Удаляем эпик с подзадачами...");
        taskManager.removeEpic(epic.getId());

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
