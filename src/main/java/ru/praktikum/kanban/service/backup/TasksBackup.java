package ru.praktikum.kanban.service.backup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import ru.praktikum.kanban.model.entity.Task;

@Getter
@EqualsAndHashCode
@ToString
public class TasksBackup {
    public final TasksContainer tasksContainer;
    public final List<Task> history;

    public TasksBackup() {
        this(new TasksContainer(), new ArrayList<>());
    }

    public TasksBackup(TasksContainer tasksContainer, List<Task> history) {
        this.tasksContainer = tasksContainer;
        this.history = history;
    }

    public List<Task> getTasksList() {
        return Stream.of(
                        tasksContainer.epics.values(),
                        tasksContainer.subtasks.values(),
                        tasksContainer.tasks.values()
                )
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }
}
