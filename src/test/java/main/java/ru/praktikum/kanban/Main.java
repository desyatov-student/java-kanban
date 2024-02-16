package main.java.ru.praktikum.kanban;

import main.java.ru.praktikum.kanban.model.TaskStatus;
import main.java.ru.praktikum.kanban.model.dto.create.CreateEpicDto;
import main.java.ru.praktikum.kanban.model.dto.create.CreateSubtaskDto;
import main.java.ru.praktikum.kanban.model.dto.create.CreateTaskDto;
import main.java.ru.praktikum.kanban.model.dto.response.EpicDto;
import main.java.ru.praktikum.kanban.model.dto.response.SubtaskDto;
import main.java.ru.praktikum.kanban.model.dto.response.TaskDto;
import main.java.ru.praktikum.kanban.model.dto.update.UpdateSubtaskDto;
import main.java.ru.praktikum.kanban.service.TaskManager;
import main.java.ru.praktikum.kanban.service.impl.Managers;

public class Main {

    public static void main(String[] args) {

        /*
            Полное тестирование функционала смотрите в тестах
        */

        TaskManager taskManager = Managers.getDefault();

        EpicDto epic = taskManager.createEpic(
                new CreateEpicDto("Техническое задание", "Технические задание 4 спринта")
        );
        SubtaskDto subtask1 = taskManager.createSubtask(
                new CreateSubtaskDto("Проектирование", "Спроектировать хранение данных", epic.getId())
        );
        SubtaskDto subtask2 = taskManager.createSubtask(
                new CreateSubtaskDto("Тест", "Написать тесты заглушки", epic.getId())
        );
        SubtaskDto subtask3 = taskManager.createSubtask(
                new CreateSubtaskDto("Тест", "Написать тесты заглушки", epic.getId())
        );

        SubtaskDto subtask4 = taskManager.createSubtask(
                new CreateSubtaskDto("Код", "Написать код", epic.getId())
        );

        System.out.println("Создан новый эпик с подзадачами:");
        System.out.println(taskManager.getEpic(epic.getId()));

        System.out.println("Создана задача:");
        TaskDto task = taskManager.createTask(
                new CreateTaskDto("Опрос", "Пройти опрос после технического задания")
        );
        System.out.println(taskManager.getAllTasks());

        System.out.println("Подзадача 1 завершена. Обновленный эпик:");
        taskManager.updateSubtask(
                new UpdateSubtaskDto(subtask1.getId(), subtask1.getName(), subtask1.getDescription(), TaskStatus.DONE)
        );
        System.out.println(taskManager.getEpic(epic.getId()));

        System.out.println("Удалены все подзадачи подзадачами:");
        taskManager.removeAllSubtasks();
        System.out.println(taskManager.getEpic(epic.getId()));
    }
}
