
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.praktikum.kanban.model.TaskStatus;
import ru.praktikum.kanban.model.entity.BaseTaskEntity;
import ru.praktikum.kanban.model.entity.EpicEntity;
import ru.praktikum.kanban.model.entity.SubtaskEntity;
import ru.praktikum.kanban.model.entity.TaskEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class EntityTest {

    @ParameterizedTest
    @MethodSource("provideModels")
    void testEquals(BaseTaskEntity task1, BaseTaskEntity task2, boolean isEquals) {
        if (isEquals) {
            assertEquals(task1, task2);
        } else {
            assertNotEquals(task1, task2);
        }
    }

    private static Stream<Arguments> provideModels() {
        return Stream.of(
                Arguments.of(
                        new EpicEntity(1, "", "", TaskStatus.NEW),
                        new EpicEntity(1, "1", "1", TaskStatus.DONE),
                        true
                ),
                Arguments.of(
                        new TaskEntity(1, "", "", TaskStatus.NEW),
                        new EpicEntity(1, "1", "1", TaskStatus.DONE),
                        true
                ),
                Arguments.of(
                        new SubtaskEntity(1, "", "", TaskStatus.NEW),
                        new EpicEntity(1, "1", "1", TaskStatus.DONE),
                        true
                ),
                Arguments.of(
                        new SubtaskEntity(1, "", "", TaskStatus.NEW),
                        new TaskEntity(1, "1", "1", TaskStatus.DONE),
                        true
                )
                ,
                Arguments.of(
                        new SubtaskEntity(1, "", "", TaskStatus.NEW),
                        new SubtaskEntity(2, "", "", TaskStatus.NEW),
                        false
                )
        );
    }
}
