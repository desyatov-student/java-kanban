package main;

import main.model.TaskStatus;
import main.model.dto.create.CreateEpicDto;
import main.model.dto.create.CreateSubtaskDto;
import main.model.dto.create.CreateTaskDto;
import main.model.dto.response.EpicDto;
import main.model.dto.response.SubtaskDto;
import main.model.dto.response.TaskDto;
import main.model.dto.update.UpdateSubtaskDto;
import main.repository.impl.TaskRepositoryInMemory;
import main.service.TaskManagerService;
import main.util.IdentifierGenerator;
import main.util.MappingUtils;

public class Main {

    public static void main(String[] args) {

        /*
            Полное тестирование функционала смотрите в тестах
        */

        TaskManagerService taskManager = new TaskManagerService(
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
