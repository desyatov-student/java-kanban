package ru.praktikum.kanban.repository.impl;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import ru.praktikum.kanban.model.backed.file.TasksBackup;
import ru.praktikum.kanban.model.entity.BaseTaskEntity;
import ru.praktikum.kanban.model.mapper.BaseTaskEntityMapper;
import ru.praktikum.kanban.model.mapper.CollectionsHelper;

import static ru.praktikum.kanban.constant.DelimiterConstants.DELIMITER_COMMA;

public class TasksCsvWriter {
    BaseTaskEntityMapper taskMapper;

    public TasksCsvWriter(BaseTaskEntityMapper taskMapper) {
        this.taskMapper = taskMapper;
    }

    public void write(TasksBackup backup, BufferedWriter bufferedWriter) throws IOException {
        List<BaseTaskEntity> tasks = CollectionsHelper.mapToList(backup.getTasks());
        List<BaseTaskEntity> history = backup.getHistory();

        for (BaseTaskEntity task : tasks) {
            bufferedWriter.newLine();
            String line = taskMapper.toString(task);
            bufferedWriter.write(line);
        }
        if (!history.isEmpty()) {
            bufferedWriter.newLine();
            String joined = history.stream()
                    .map(BaseTaskEntity::getId)
                    .map(String::valueOf)
                    .collect(Collectors.joining(DELIMITER_COMMA));
            bufferedWriter.write(joined);
        }
    }
}
