package ru.praktikum.kanban;

import ru.praktikum.kanban.model.TaskStatus;
import ru.praktikum.kanban.model.dto.create.CreateEpicDto;
import ru.praktikum.kanban.model.dto.create.CreateSubtaskDto;
import ru.praktikum.kanban.model.dto.create.CreateTaskDto;
import ru.praktikum.kanban.model.dto.response.EpicDto;
import ru.praktikum.kanban.model.dto.response.SubtaskDto;
import ru.praktikum.kanban.model.dto.response.TaskDto;
import ru.praktikum.kanban.model.dto.update.UpdateSubtaskDto;
import ru.praktikum.kanban.repository.impl.TaskRepositoryInMemory;
import ru.praktikum.kanban.service.TaskManager;
import ru.praktikum.kanban.service.impl.InMemoryTaskManager;
import ru.praktikum.kanban.util.IdentifierGenerator;
import ru.praktikum.kanban.util.MappingUtils;

public class Main {

    public static void main(String[] args) {

        /*
            Полное тестирование функционала смотрите в тестах
        */

        TaskManager taskManager = new InMemoryTaskManager(
                new IdentifierGenerator(),
                new TaskRepositoryInMemory(),
                new MappingUtils()
        );

        EpicDto epic = taskManager.createEpic(
                new CreateEpicDto("Техническое задание", "Технические задание 4 спринта")
        );
        SubtaskDto subtask1 = taskManager.createSubtask(
                new CreateSubtaskDto("Проектирование", "Спроектировать хранение данных"),
                epic.getId()
        );
        SubtaskDto subtask2 = taskManager.createSubtask(
                new CreateSubtaskDto("Тест", "Написать тесты заглушки"),
                epic.getId()
        );
        SubtaskDto subtask3 = taskManager.createSubtask(
                new CreateSubtaskDto("Тест", "Написать тесты заглушки"),
                epic.getId()
        );

        SubtaskDto subtask4 = taskManager.createSubtask(
                new CreateSubtaskDto("Код", "Написать код"),
                epic.getId()
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
