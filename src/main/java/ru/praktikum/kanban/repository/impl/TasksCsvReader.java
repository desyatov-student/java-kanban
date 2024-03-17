package ru.praktikum.kanban.repository.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import ru.praktikum.kanban.model.TaskType;
import ru.praktikum.kanban.model.TasksContainer;
import ru.praktikum.kanban.model.backed.file.TasksBackup;
import ru.praktikum.kanban.model.entity.BaseTaskEntity;
import ru.praktikum.kanban.model.mapper.BaseTaskEntityMapper;

import static ru.praktikum.kanban.constant.DelimiterConstants.DELIMITER_COMMA;

public class TasksCsvReader {
    BaseTaskEntityMapper taskMapper;

    public TasksCsvReader(BaseTaskEntityMapper taskMapper) {
        this.taskMapper = taskMapper;
    }

    public TasksBackup read(BufferedReader bufferedReader) throws IOException {

        TasksContainer tasksContainer = new TasksContainer();
        HashMap<Integer, BaseTaskEntity> allTasks = new HashMap<>();
        Stream<Integer> historyStream = Stream.empty();

        bufferedReader.readLine(); // skip first line with titles
        while (bufferedReader.ready()) {
            String line = bufferedReader.readLine();
            String[] values = line.split(DELIMITER_COMMA);

            TaskType taskType;
            try {
                taskType = TaskType.valueOf(values[1]);
            } catch (Exception e) {
                taskType = null;
            }

            if (taskType != null) {
                BaseTaskEntity task = taskMapper.toModel(
                        new BaseTaskEntityMapper.Input(values, taskType, tasksContainer)
                );
                allTasks.put(task.getId(), task);
            } else {
                historyStream = Stream.of(values).map(Integer::valueOf);
            }
        }
        List<BaseTaskEntity> history = historyStream.map(allTasks::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        return new TasksBackup(tasksContainer, history);
    }

}
