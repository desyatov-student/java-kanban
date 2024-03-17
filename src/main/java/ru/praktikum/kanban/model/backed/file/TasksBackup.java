package ru.praktikum.kanban.model.backed.file;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import ru.praktikum.kanban.model.TasksContainer;
import ru.praktikum.kanban.model.entity.BaseTaskEntity;

@Getter
@EqualsAndHashCode
@ToString
public class TasksBackup {
    TasksContainer tasksContainer;
    List<BaseTaskEntity> history;

    public TasksBackup() {
        this(new TasksContainer(), new ArrayList<>());
    }

    public TasksBackup(TasksContainer tasksContainer, List<BaseTaskEntity> history) {
        this.tasksContainer = tasksContainer;
        this.history = history;
    }

    public List<BaseTaskEntity> getTasksList() {
        return Stream.of(
                        tasksContainer.epics.values(),
                        tasksContainer.subtasks.values(),
                        tasksContainer.tasks.values()
                )
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }
}
