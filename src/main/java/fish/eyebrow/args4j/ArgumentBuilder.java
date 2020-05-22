package fish.eyebrow.args4j;

import fish.eyebrow.args4j.annotations.Flag;
import fish.eyebrow.args4j.annotations.Option;
import fish.eyebrow.args4j.exceptions.MultiAnnotatedFieldException;
import fish.eyebrow.args4j.exceptions.UnsupportedFlagTypeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ArgumentBuilder {
    private static final Logger logger = LoggerFactory.getLogger(ArgumentBuilder.class);

    private ArgumentBuilder() { }

    public static <T> void scan(final Class<T> clazz, final String... args) {
        final var argsList = Arrays.asList(args);

        Arrays.stream(clazz.getDeclaredFields())
                .filter(AccessibleObject::trySetAccessible)
                .peek(field -> {
                    if (field.isAnnotationPresent(Flag.class) && field.isAnnotationPresent(Option.class)) {
                        throw new MultiAnnotatedFieldException(field + " has too many annotations");
                    }
                })
                .forEach(field -> {
                    if (argsContainFlag(argsList, field)) {
                        applyFlagMutation(clazz, field);
                    } else if (field.isAnnotationPresent(Option.class)) {
                        final var optionValueIndex = indexOfOptionValue(argsList, field);

                        if (optionValueIndex > 0) {
                            applyOptionMutation(clazz, field, argsList.get(optionValueIndex));
                        }
                    }
                });
    }

    private static boolean argsContainFlag(final List<String> argsList, final Field field) {
        final var annotation = field.getAnnotation(Flag.class);
        return Objects.nonNull(annotation) && (
                argsList.contains(Constants.LONG_ARG_PREFIX + field.getName()) || (
                        !annotation.shortName().equals(Constants.SHORT_NAME_DEFAULT) &&
                        argsList.contains(Constants.SHORT_ARG_PREFIX + annotation.shortName())
                )
        );
    }

    private static int indexOfOptionValue(final List<String> argsList, final Field field) {
        final var found = argsList.indexOf(Constants.LONG_ARG_PREFIX + field.getName());
        if (found > -1) {
            return found < argsList.size() ? found + 1 : -1;
        } else {
            final var annotation = field.getAnnotation(Option.class);
            final var shortName = annotation.shortName();
            final var shortNameFound = argsList.indexOf(Constants.SHORT_ARG_PREFIX + shortName);

            return shortNameFound > -1 && !shortName.equals(Constants.SHORT_NAME_DEFAULT) ? shortNameFound + 1 : -1;
        }
    }

    private static <T> void applyFlagMutation(final Class<T> clazz, final Field field) {
        try {
            if (field.getType().isAssignableFrom(boolean.class)) {
                field.setBoolean(clazz, true);
            } else if (field.getType().isAssignableFrom(int.class)) {
                field.setInt(clazz, 1);
            } else {
                throw new UnsupportedFlagTypeException(
                        field.getType().getSimpleName() + " is not a supported flag type"
                );
            }
        } catch (final IllegalAccessException e) {
            logger.error("Exception occurred when setting flag: {}", field.getName());
        }
    }

    private static <T> void applyOptionMutation(final Class<T> clazz, final Field field, final String value) {
        try {
            field.set(clazz, value);
        } catch (final IllegalAccessException e) {
            logger.error("Exception occurred when setting option: {}", field.getName());
        }
    }
}
