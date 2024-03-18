package ru.praktikum.kanban.service.impl;

import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.praktikum.kanban.service.impl.HistoryLinkedList;
import ru.praktikum.kanban.model.TaskStatus;
import ru.praktikum.kanban.model.Task;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HistoryLinkedListTest {

    HistoryLinkedList historyLinkedList;
    @BeforeEach
    void setupList() {
        historyLinkedList = new HistoryLinkedList();
    }

    @ParameterizedTest
    @MethodSource("provideModels")
    void testAdd(List<Task> expected, List<Task> given, List<Task> input) {
        for (Task entity : given) {
            historyLinkedList.add(entity);
        }
        for (Task entity : input) {
            historyLinkedList.add(entity);
        }
        assertEquals(expected, historyLinkedList.values());
    }

    @ParameterizedTest
    @MethodSource("provideModelsForRemoveTest")
    void testRemove(List<Task> expected, List<Task> given, int idToRemove) {
        for (Task entity : given) {
            historyLinkedList.add(entity);
        }
        historyLinkedList.remove(idToRemove);
        assertEquals(expected, historyLinkedList.values());
    }

    @Test
    void testRemoveAndAddValues() {
        historyLinkedList.add(TASK(1, "name1"));
        historyLinkedList.add(TASK(2, "name2"));
        historyLinkedList.add(TASK(3, "name3"));
        historyLinkedList.add(TASK(4, "name4"));
        historyLinkedList.add(TASK(5, "name5"));
        historyLinkedList.remove(3);
        historyLinkedList.remove(2);
        historyLinkedList.remove(4);


        historyLinkedList.add(TASK(3, "name3_new"));
        historyLinkedList.add(TASK(2, "name2_new"));

        assertEquals(4, historyLinkedList.size());
        assertEquals(
                List.of(
                        TASK(1, "name1"),
                        TASK(5, "name5"),
                        TASK(3, "name3_new"),
                        TASK(2, "name2_new")
                ),
                historyLinkedList.values()
        );
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
                        List.of(TASK(1, "name2")),
                        List.of(TASK(1)),
                        List.of(TASK(1, "name2"))
                ),
                Arguments.of(
                        List.of(TASK(2), TASK(1, "name2")),
                        List.of(TASK(1), TASK(2)),
                        List.of(TASK(1, "name2"))
                ),
                Arguments.of(
                        List.of(TASK(2), TASK(3), TASK(1, "name2")),
                        List.of(TASK(1), TASK(2), TASK(3)),
                        List.of(TASK(1, "name2"))
                ),
                Arguments.of(
                        List.of(TASK(1), TASK(2), TASK(3, "name2")),
                        List.of(TASK(1), TASK(2), TASK(3)),
                        List.of(TASK(3, "name2"))
                ),
                Arguments.of(
                        List.of(TASK(1), TASK(3), TASK(2, "name2")),
                        List.of(TASK(1), TASK(2), TASK(3)),
                        List.of(TASK(2, "name2"))
                ),
                Arguments.of(
                        List.of(TASK(1), TASK(2), TASK(3), TASK(4, "name2")),
                        List.of(TASK(1), TASK(2), TASK(3)),
                        List.of(TASK(4, "name2"))
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

    private static Task TASK(int id, String name) { return new Task(id, name, "", TaskStatus.NEW); }
    private static Task TASK(int id) { return TASK(id, ""); }
}
