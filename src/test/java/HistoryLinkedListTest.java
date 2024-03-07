import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.praktikum.kanban.model.HistoryLinkedList;
import ru.praktikum.kanban.model.TaskStatus;
import ru.praktikum.kanban.model.entity.BaseTaskEntity;
import ru.praktikum.kanban.model.entity.TaskEntity;
import ru.praktikum.kanban.util.AbstractMapper;

import static org.junit.jupiter.api.Assertions.*;

class HistoryLinkedListTest {

    HistoryLinkedList historyLinkedList;
    @BeforeEach
    void setupList() {
        historyLinkedList = new HistoryLinkedList();
    }

    @ParameterizedTest
    @MethodSource("provideModels")
    void testAdd(List<BaseTaskEntity> expected, List<BaseTaskEntity> given, List<BaseTaskEntity> input) {
        for (BaseTaskEntity entity : given) {
            historyLinkedList.add(entity);
        }
        for (BaseTaskEntity entity : input) {
            historyLinkedList.add(entity);
        }
        assertEquals(expected, historyLinkedList.values());
    }

    @ParameterizedTest
    @MethodSource("provideModelsForRemoveTest")
    void testRemove(List<BaseTaskEntity> expected, List<BaseTaskEntity> given, int idToRemove) {
        for (BaseTaskEntity entity : given) {
            historyLinkedList.add(entity);
        }
        historyLinkedList.remove(idToRemove);
        assertEquals(expected, historyLinkedList.values());
    }

    private static Stream<Arguments> provideModels() {
        return Stream.of(
                Arguments.of(
                        List.of(),
                        List.of(),
                        List.of()
                ),
                Arguments.of(
                        List.of(TASK(1)),
                        List.of(),
                        List.of(TASK(1))
                ),
                Arguments.of(
                        List.of(TASK(1)),
                        List.of(TASK(1)),
                        List.of(TASK(1))
                ),
                Arguments.of(
                        List.of(TASK(2), TASK(1)),
                        List.of(TASK(1), TASK(2)),
                        List.of(TASK(1))
                ),
                Arguments.of(
                        List.of(TASK(2), TASK(3), TASK(1)),
                        List.of(TASK(1), TASK(2), TASK(3)),
                        List.of(TASK(1))
                ),
                Arguments.of(
                        List.of(TASK(1), TASK(2), TASK(3)),
                        List.of(TASK(1), TASK(2), TASK(3)),
                        List.of(TASK(3))
                ),
                Arguments.of(
                        List.of(TASK(1), TASK(3), TASK(2)),
                        List.of(TASK(1), TASK(2), TASK(3)),
                        List.of(TASK(2))
                ),
                Arguments.of(
                        List.of(TASK(1), TASK(2), TASK(3), TASK(4)),
                        List.of(TASK(1), TASK(2), TASK(3)),
                        List.of(TASK(4))
                )
        );
    }

    private static Stream<Arguments> provideModelsForRemoveTest() {
        return Stream.of(
                Arguments.of(
                        List.of(),
                        List.of(),
                        1
                ),
                Arguments.of(
                        List.of(),
                        List.of(TASK(1)),
                        1
                ),
                Arguments.of(
                        List.of(TASK(1)),
                        List.of(TASK(1)),
                        2
                ),
                Arguments.of(
                        List.of(TASK(2)),
                        List.of(TASK(1), TASK(2)),
                        1
                ),
                Arguments.of(
                        List.of(TASK(1)),
                        List.of(TASK(1), TASK(2)),
                        2
                ),
                Arguments.of(
                        List.of(TASK(1), TASK(3)),
                        List.of(TASK(1), TASK(2), TASK(3)),
                        2
                ),
                Arguments.of(
                        List.of(TASK(1), TASK(2)),
                        List.of(TASK(1), TASK(2), TASK(3)),
                        3
                ),
                Arguments.of(
                        List.of(TASK(2), TASK(3)),
                        List.of(TASK(1), TASK(2), TASK(3)),
                        1
                )
        );
    }

    private static TaskEntity TASK(int id) { return new TaskEntity(id, "", "", TaskStatus.NEW); }
}