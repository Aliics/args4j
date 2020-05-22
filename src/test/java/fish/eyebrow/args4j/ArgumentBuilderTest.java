package fish.eyebrow.args4j;

import fish.eyebrow.args4j.annotations.Flag;
import fish.eyebrow.args4j.exceptions.UnsupportedFlagTypeException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ArgumentBuilderTest {
    @AfterEach
    void tearDown() {
        SingletonClass.reset();
        UnsupportedFlagTypeSingletonClass.reset();
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

    @Test
    void annotatedFlagUnsupported() {
        assertThrows(
                UnsupportedFlagTypeException.class,
                () -> ArgumentBuilder.scan(UnsupportedFlagTypeSingletonClass.class)
        );
    }

    static class SingletonClass {
        @Flag
        private static boolean flag;
        @Flag
        private static int flagInt;

        private SingletonClass() {}

        private static void reset() {
            flag = false;
            flagInt = 0;
        }
    }

    static class UnsupportedFlagTypeSingletonClass {
        @Flag
        private static String badFlag;

        private UnsupportedFlagTypeSingletonClass() {}

        private static void reset() {
            badFlag = null;
        }
    }
}