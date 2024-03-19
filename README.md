# Менеджер задач

> [!IMPORTANT]
> В моей реализации генерация `id` происходить внутри `TaskManager` и снаружи не передается. Я считаю это лишнее знание для создания задачи.

## Создание задач

Создание эпика:
```Java
CreateEpicDto newEpic = new CreateEpicDto("Имя", "описание");
```
Создание подзадачи:
```Java
CreateSubtaskDto newSubtask = new CreateSubtaskDto("Имя", "описание");
```
Создание задачи:
```Java
CreateTaskDto newTask = new CreateTaskDto("Имя", "описание");
```
 ## Обновление задач
Обновление эпика:
```Java
UpdateEpicDto updateEpicDto = new UpdateEpicDto(10, "Имя", "описание");
```
Обновление подзадачи:
```Java
UpdateSubtaskDto updateSubtaskDto = new UpdateSubtaskDto(11, "Имя", "описание", TaskStatus.DONE);
```
Создание задачи:
```Java
UpdateTaskDto updateTaskDto = new UpdateTaskDto(12, "Имя", "описание", TaskStatus.DONE);
```
