package fish.eyebrow.args4j;

import fish.eyebrow.args4j.annotations.Flag;
import fish.eyebrow.args4j.annotations.Option;
import fish.eyebrow.args4j.exceptions.MultiAnnotatedFieldException;
import fish.eyebrow.args4j.exceptions.UnsupportedFlagTypeException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ArgumentBuilderTest {
    private ArgumentBuilder argumentBuilder;

    @BeforeEach
    void setUp() {
        argumentBuilder = new ArgumentBuilder();
    }

    @AfterEach
    void tearDown() {
        SingletonClass.reset();
        UnsupportedFlagTypeSingletonClass.reset();
        BadMultiAnnotationSingletonClass.reset();
    }

    @Test
    void annotatedFlag() {
        final var args = new String[]{"--flag"};

        argumentBuilder.scan(SingletonClass.class, args);

        assertTrue(SingletonClass.flag);
        assertEquals(0, SingletonClass.flagInt);
        assertFalse(SingletonClass.flagShortName);
        assertNull(SingletonClass.option);
        assertNull(SingletonClass.optionShortName);
    }

    @Test
    void annotatedFlagInt() {
        final var args = new String[]{"--flagInt"};

        argumentBuilder.scan(SingletonClass.class, args);

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
                () -> argumentBuilder.scan(UnsupportedFlagTypeSingletonClass.class, args)
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

        argumentBuilder.scan(SingletonClass.class, args);

        assertFalse(SingletonClass.flag);
        assertEquals(0, SingletonClass.flagInt);
        assertTrue(SingletonClass.flagShortName);
        assertNull(SingletonClass.option);
        assertNull(SingletonClass.optionShortName);
    }

    @Test
    void givenOption() {
        final var args = new String[]{"--option", "awesome"};

        argumentBuilder.scan(SingletonClass.class, args);

        assertFalse(SingletonClass.flag);
        assertEquals(0, SingletonClass.flagInt);
        assertFalse(SingletonClass.flagShortName);
        assertEquals("awesome", SingletonClass.option);
        assertNull(SingletonClass.optionShortName);
    }

    @Test
    void shortOptionName() {
        final var args = new String[]{"-o", "bunny"};

        argumentBuilder.scan(SingletonClass.class, args);

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
                () -> argumentBuilder.scan(BadMultiAnnotationSingletonClass.class, args)
        );
        assertFalse(SingletonClass.flag);
        assertEquals(0, SingletonClass.flagInt);
        assertFalse(SingletonClass.flagShortName);
        assertNull(SingletonClass.option);
        assertNull(SingletonClass.optionShortName);
    }

    @Test
    void generatedHelp() throws IOException {
        final var outputBuilder = new StringBuilder();
        final var args = new String[]{"--help"};
        argumentBuilder = ArgumentBuilder.builder()
                .setOutputMethod(outputBuilder::append)
                .build();

        argumentBuilder.scan(SingletonClass.class, args);

        assertNotEquals("", outputBuilder.toString());
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
