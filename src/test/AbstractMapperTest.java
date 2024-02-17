import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import ru.praktikum.kanban.model.TaskStatus;
import ru.praktikum.kanban.model.dto.response.BaseTaskDto;
import ru.praktikum.kanban.model.dto.response.TaskDto;
import ru.praktikum.kanban.model.entity.BaseTaskEntity;
import ru.praktikum.kanban.model.entity.EpicEntity;
import ru.praktikum.kanban.model.entity.TaskEntity;
import ru.praktikum.kanban.util.AbstractMapper;
import ru.praktikum.kanban.util.MappingUtils;

import static org.junit.jupiter.api.Assertions.*;

class AbstractMapperTest {

    AbstractMapper<BaseTaskEntity, BaseTaskDto> abstractMapper;

    @BeforeEach
    void setupMapper() {
        abstractMapper = new AbstractMapper<>();
    }

    @Test
    void shouldMapToTaskEntityToTaskDto() {
        abstractMapper.put(TaskEntity.class, (value) -> MappingUtils.mapToTaskDto((TaskEntity) value));

        TaskEntity taskEntity = new TaskEntity(1, "", "", TaskStatus.NEW);
        TaskDto expected = new TaskDto(taskEntity.getId(), taskEntity.name, taskEntity.description, taskEntity.status);
        BaseTaskDto actual = abstractMapper.tryMap(taskEntity);
        assertEquals(expected, actual);
    }

    @Test
    void shouldBeThrowAndInputIsNull() {
        assertThrows(IllegalArgumentException.class, () -> abstractMapper.tryMap(null));
    }

    @Test
    void shouldBeThrowAndActionIsNull() {
        Executable executable = () -> abstractMapper.put(TaskEntity.class, null);
        assertThrows(IllegalArgumentException.class, executable);
    }

    @Test
    void shouldBeThrowAndClassIsNull() {
        Executable executable = () -> abstractMapper.put(null, (value) -> MappingUtils.mapToTaskDto((TaskEntity) value));
        assertThrows(IllegalArgumentException.class, executable);
    }

    @Test
    void shouldBeThrowAndAbstractMapperHasNoClassAndNoFunction() {
        EpicEntity epicEntity = new EpicEntity(1, "", "", TaskStatus.NEW);
        assertThrows(IllegalArgumentException.class, () -> abstractMapper.tryMap(epicEntity));
    }
}
