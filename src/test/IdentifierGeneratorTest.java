package test;

import ru.praktikum.kanban.util.IdentifierGenerator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IdentifierGeneratorTest {

    @Test
    void getNextId() {
        IdentifierGenerator generator = new IdentifierGenerator();
        assertEquals(IdentifierGenerator.INITIAL_IDENTIFIER, generator.getNextId());
        generator.getNextId();
        generator.getNextId();
        generator.getNextId();
        assertEquals(5, generator.getNextId());
    }
}
