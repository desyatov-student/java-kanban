import org.junit.jupiter.api.Test;
import ru.praktikum.kanban.model.TaskStatus;
import ru.praktikum.kanban.model.dto.response.BaseTaskDto;
import ru.praktikum.kanban.model.dto.response.TaskDto;
import ru.praktikum.kanban.model.entity.BaseTaskEntity;
import ru.praktikum.kanban.model.entity.EpicEntity;
import ru.praktikum.kanban.model.entity.TaskEntity;
import ru.praktikum.kanban.util.EntityMapper;
import ru.praktikum.kanban.util.MappingUtils;

import static org.junit.jupiter.api.Assertions.*;

class EntityMapperTest {

    @Test
    void mapToTaskDto() {
        EntityMapper<BaseTaskEntity, BaseTaskDto> entityMapper = new EntityMapper<>();
        entityMapper.put(TaskEntity.class, (value) -> MappingUtils.mapToTaskDto((TaskEntity) value));

        TaskEntity taskEntity = new TaskEntity(1, "", "", TaskStatus.NEW);
        BaseTaskDto taskDto = entityMapper.mapToTaskDto(taskEntity);
        assertEquals(new TaskDto(1, "", "", TaskStatus.NEW), taskDto);

        EpicEntity epicEntity = new EpicEntity(1, "", "", TaskStatus.NEW);
        assertThrows(IllegalArgumentException.class, () -> entityMapper.mapToTaskDto(epicEntity));
        assertThrows(IllegalArgumentException.class, () -> entityMapper.mapToTaskDto(null));
    }
}
