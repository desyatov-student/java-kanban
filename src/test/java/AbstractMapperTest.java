import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import ru.praktikum.kanban.model.TaskStatus;
import ru.praktikum.kanban.model.dto.response.BaseTaskDto;
import ru.praktikum.kanban.model.dto.response.TaskDto;
import ru.praktikum.kanban.model.entity.BaseTaskEntity;
import ru.praktikum.kanban.model.entity.EpicEntity;
import ru.praktikum.kanban.model.entity.TaskEntity;
import ru.praktikum.kanban.model.mapper.TaskMapper;
import ru.praktikum.kanban.model.mapper.TaskMapperImpl;
import ru.praktikum.kanban.util.AbstractMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AbstractMapperTest {

    AbstractMapper<BaseTaskEntity, BaseTaskDto> abstractMapper;
    TaskMapper taskMapper = new TaskMapperImpl();

    @BeforeEach
    public void setupMapper() {
        abstractMapper = new AbstractMapper<>();
    }

    @Test
    public void shouldMapToTaskEntityToTaskDto() {
        abstractMapper.put(TaskEntity.class, (value) -> taskMapper.toDto((TaskEntity) value));

        TaskEntity taskEntity = new TaskEntity(1, "", "", TaskStatus.NEW);
        TaskDto expected = new TaskDto(taskEntity.getId(), taskEntity.name, taskEntity.description, taskEntity.status);
        BaseTaskDto actual = abstractMapper.tryMap(taskEntity);
        assertEquals(expected, actual);
    }

    @Test
    public void shouldBeThrowAndInputIsNull() {
        assertThrows(IllegalArgumentException.class, () -> abstractMapper.tryMap(null));
    }

    @Test
    public void shouldBeThrowAndActionIsNull() {
        Executable executable = () -> abstractMapper.put(TaskEntity.class, null);
        assertThrows(IllegalArgumentException.class, executable);
    }

    @Test
    public void shouldBeThrowAndClassIsNull() {
        Executable executable = () -> abstractMapper.put(null, (value) -> taskMapper.toDto((TaskEntity) value));
        assertThrows(IllegalArgumentException.class, executable);
    }

    @Test
    public void shouldBeThrowAndAbstractMapperHasNoClassAndNoFunction() {
        EpicEntity epicEntity = new EpicEntity(1, "", "", TaskStatus.NEW);
        assertThrows(IllegalArgumentException.class, () -> abstractMapper.tryMap(epicEntity));
    }
}
