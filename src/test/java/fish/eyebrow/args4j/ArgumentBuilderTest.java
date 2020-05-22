package fish.eyebrow.args4j;

import fish.eyebrow.args4j.annotations.Flag;
import fish.eyebrow.args4j.annotations.Option;
import fish.eyebrow.args4j.exceptions.MultiAnnotatedFieldException;
import fish.eyebrow.args4j.exceptions.UnsupportedFlagTypeException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ArgumentBuilderTest {
    @AfterEach
    void tearDown() {
        SingletonClass.reset();
        UnsupportedFlagTypeSingletonClass.reset();
        BadMultiAnnotationSingletonClass.reset();
    }

    @Test
    void annotatedFlag() {
        final var args = new String[]{"--flag"};

        ArgumentBuilder.scan(SingletonClass.class, args);

        assertTrue(SingletonClass.flag);
        assertEquals(0, SingletonClass.flagInt);
        assertFalse(SingletonClass.flagShortName);
        assertNull(SingletonClass.option);
        assertNull(SingletonClass.optionShortName);
    }

    @Test
    void annotatedFlagInt() {
        final var args = new String[]{"--flagInt"};

        ArgumentBuilder.scan(SingletonClass.class, args);

        assertFalse(SingletonClass.flag);
        assertEquals(1, SingletonClass.flagInt);
        assertFalse(SingletonClass.flagShortName);
        assertNull(SingletonClass.option);
        assertNull(SingletonClass.optionShortName);
    }

    @Test
    void annotatedFlagUnsupported() {
        final var args = new String[]{"--badFlag"};

        assertThrows(
                UnsupportedFlagTypeException.class,
                () -> ArgumentBuilder.scan(UnsupportedFlagTypeSingletonClass.class, args)
        );
        assertFalse(SingletonClass.flag);
        assertEquals(0, SingletonClass.flagInt);
        assertFalse(SingletonClass.flagShortName);
        assertNull(SingletonClass.option);
        assertNull(SingletonClass.optionShortName);
    }

    @Test
    void shortFlagName() {
        final var args = new String[]{"-s"};

        ArgumentBuilder.scan(SingletonClass.class, args);

        assertFalse(SingletonClass.flag);
        assertEquals(0, SingletonClass.flagInt);
        assertTrue(SingletonClass.flagShortName);
        assertNull(SingletonClass.option);
        assertNull(SingletonClass.optionShortName);
    }

    @Test
    void givenOption() {
        final var args = new String[]{"--option", "awesome"};

        ArgumentBuilder.scan(SingletonClass.class, args);

        assertFalse(SingletonClass.flag);
        assertEquals(0, SingletonClass.flagInt);
        assertFalse(SingletonClass.flagShortName);
        assertEquals("awesome", SingletonClass.option);
        assertNull(SingletonClass.optionShortName);
    }

    @Test
    void shortOptionName() {
        final var args = new String[]{"-o", "bunny"};

        ArgumentBuilder.scan(SingletonClass.class, args);

        assertFalse(SingletonClass.flag);
        assertEquals(0, SingletonClass.flagInt);
        assertFalse(SingletonClass.flagShortName);
        assertNull(SingletonClass.option);
        assertEquals("bunny", SingletonClass.optionShortName);
    }

    @Test
    void badMultiAnnotatedField() {
        final var args = new String[]{"--badField", "wow"};

        assertThrows(
                MultiAnnotatedFieldException.class,
                () -> ArgumentBuilder.scan(BadMultiAnnotationSingletonClass.class, args)
        );
        assertFalse(SingletonClass.flag);
        assertEquals(0, SingletonClass.flagInt);
        assertFalse(SingletonClass.flagShortName);
        assertNull(SingletonClass.option);
        assertNull(SingletonClass.optionShortName);
    }

    static class SingletonClass {
        @Flag
        private static boolean flag;
        @Flag
        private static int flagInt;
        @Flag(shortName = "s")
        private static boolean flagShortName;
        @Option
        private static String option;
        @Option(shortName = "o")
        private static String optionShortName;

        private SingletonClass() {}

        private static void reset() {
            flag = false;
            flagInt = 0;
            flagShortName = false;
            option = null;
            optionShortName = null;
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

    static class BadMultiAnnotationSingletonClass {
        @Flag
        @Option
        private static int badField;

        private BadMultiAnnotationSingletonClass() {}

        private static void reset() {
            badField = 0;
        }
    }
}
