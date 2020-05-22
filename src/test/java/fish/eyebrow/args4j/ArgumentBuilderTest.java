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
        final var args = new String[]{"--flag"};

        ArgumentBuilder.scan(SingletonClass.class, args);

        assertTrue(SingletonClass.flag);
        assertEquals(0, SingletonClass.flagInt);
        assertFalse(SingletonClass.flagShortName);
    }

    @Test
    void annotatedFlagInt() {
        final var args = new String[]{"--flagInt"};

        ArgumentBuilder.scan(SingletonClass.class, args);

        assertFalse(SingletonClass.flag);
        assertEquals(1, SingletonClass.flagInt);
        assertFalse(SingletonClass.flagShortName);
    }

    @Test
    void annotatedFlagUnsupported() {
        final var args = new String[]{"--badFlag"};

        assertThrows(
                UnsupportedFlagTypeException.class,
                () -> ArgumentBuilder.scan(UnsupportedFlagTypeSingletonClass.class, args)
        );
    }

    @Test
    void shortFlagNames() {
        final var args = new String[]{"-s"};

        ArgumentBuilder.scan(SingletonClass.class, args);

        assertFalse(SingletonClass.flag);
        assertEquals(0, SingletonClass.flagInt);
        assertTrue(SingletonClass.flagShortName);
    }

    static class SingletonClass {
        @Flag
        private static boolean flag;
        @Flag
        private static int flagInt;
        @Flag(shortName = "s")
        private static boolean flagShortName;

        private SingletonClass() {}

        private static void reset() {
            flag = false;
            flagInt = 0;
            flagShortName = false;
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
