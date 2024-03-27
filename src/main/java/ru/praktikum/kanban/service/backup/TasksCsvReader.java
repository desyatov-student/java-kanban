package ru.praktikum.kanban.service.backup;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import ru.praktikum.kanban.service.mapper.TaskType;
import ru.praktikum.kanban.model.Task;
import ru.praktikum.kanban.service.mapper.AdvancedTaskMapper;

import static ru.praktikum.kanban.constant.DelimiterConstants.DELIMITER_COMMA;

public class TasksCsvReader {
    AdvancedTaskMapper taskMapper;

    public TasksCsvReader(AdvancedTaskMapper taskMapper) {
        this.taskMapper = taskMapper;
    }

    public TasksBackup read(BufferedReader bufferedReader) throws IOException {

        TasksContainer tasksContainer = new TasksContainer();
        HashMap<Integer, Task> allTasks = new HashMap<>();
        Stream<Integer> historyStream = Stream.empty();

        bufferedReader.readLine(); // skip first line with titles
        while (bufferedReader.ready()) {
            String line = bufferedReader.readLine();
            String[] values = line.split(DELIMITER_COMMA, -1);

            TaskType taskType;
            try {
                taskType = TaskType.valueOf(values[1]);
            } catch (Exception e) {
                taskType = null;
            }

            if (taskType != null) {
                Task task = taskMapper.toModel(
                        new AdvancedTaskMapper.Input(values, taskType, tasksContainer)
                );
                allTasks.put(task.getId(), task);
            } else {
                historyStream = Stream.of(values).map(Integer::valueOf);
            }
        }
        List<Task> history = historyStream.map(allTasks::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        return new TasksBackup(tasksContainer, history);
    }

}
