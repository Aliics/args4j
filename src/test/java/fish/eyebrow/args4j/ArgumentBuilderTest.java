package fish.eyebrow.args4j;

import fish.eyebrow.args4j.annotations.Flag;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ArgumentBuilderTest {
    @AfterEach
    void tearDown() {
        SingletonClass.reset();
    }

    @Test
    void annotatedFlag() {
        ArgumentBuilder.scan(SingletonClass.class);

        assertTrue(SingletonClass.flag);
    }

    @Test
    void annotatedFlagInt() {
        ArgumentBuilder.scan(SingletonClass.class);

        assertEquals(1, SingletonClass.flagInt);
    }

    static class SingletonClass {
        @Flag
        private static boolean flag;
        @Flag
        private static int flagInt;

        private SingletonClass() {}

        // Due to this being static there needs to be a nice way to ensure
        // tests are not interfering with each other.
        private static void reset() {
            flag = false;
            flagInt = 0;
        }
    }
}