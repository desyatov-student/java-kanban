package ru.praktikum.kanban.model.mapper;

import java.util.HashMap;
import java.util.function.Function;
import ru.praktikum.kanban.model.TaskType;
import ru.praktikum.kanban.model.entity.BaseTaskEntity;
import ru.praktikum.kanban.model.entity.EpicEntity;
import ru.praktikum.kanban.model.entity.SubtaskEntity;
import ru.praktikum.kanban.model.entity.TaskEntity;
import ru.praktikum.kanban.util.AbstractMapper;

public class BaseTaskEntityMapper implements BaseTaskMapper<BaseTaskEntity> {
    AbstractMapper<BaseTaskEntity, String> entityToStringMapper;
    HashMap<TaskType, Function<Input<BaseTaskEntity>, BaseTaskEntity>> inputToEntityMapper;
    TaskMapper taskMapper;
    SubtaskMapper subtaskMapper;
    EpicMapper epicMapper;

    public BaseTaskEntityMapper() {
        this.entityToStringMapper = new AbstractMapper<>();
        this.inputToEntityMapper = new HashMap<>();
        this.taskMapper = new TaskMapperImpl();
        this.subtaskMapper = new SubtaskMapperImpl();
        this.epicMapper = new EpicMapperImpl();

        entityToStringMapper.put(TaskEntity.class, task -> taskMapper.toString((TaskEntity) task));
        entityToStringMapper.put(SubtaskEntity.class, task -> subtaskMapper.toString((SubtaskEntity) task));
        entityToStringMapper.put(EpicEntity.class, task -> epicMapper.toString((EpicEntity) task));

        inputToEntityMapper.put(TaskType.TASK, input -> taskMapper.toEntity(input.values));
        inputToEntityMapper.put(TaskType.SUBTASK, input -> {
            SubtaskEntity subtask = subtaskMapper.toEntity(input.values);
            EpicEntity epic = (EpicEntity) input.models.get(subtask.getEpicId());
            epic.subtasks.add(subtask.getEpicId());
            return subtask;
        });
        inputToEntityMapper.put(TaskType.EPIC, input -> epicMapper.toEntity(input.values));
    }

    public String toString(BaseTaskEntity task) {
        return entityToStringMapper.tryMap(task);
    }

    public BaseTaskEntity toModel(Input<BaseTaskEntity> input) {
        return inputToEntityMapper.get(input.taskType).apply(input);
    }
}

