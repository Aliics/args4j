package fish.eyebrow.args4j;

import fish.eyebrow.args4j.annotations.Flag;
import fish.eyebrow.args4j.exceptions.UnsupportedFlagTypeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.AccessibleObject;
import java.util.Arrays;

public class ArgumentBuilder {
    private static final Logger logger = LoggerFactory.getLogger(ArgumentBuilder.class);

    private ArgumentBuilder() { }

    public static <T> void scan(final Class<T> clazz, final String... args) {
        final var argsList = Arrays.asList(args);

        Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Flag.class))
                .filter(AccessibleObject::trySetAccessible)
                .filter(field -> {
                    final var flagAnnotation = field.getAnnotation(Flag.class);
                    return argsList.contains(Constants.LONG_ARG_PREFIX + field.getName()) ||
                           (
                                   !flagAnnotation.shortName().equals(Constants.SHORT_NAME_DEFAULT) &&
                                   argsList.contains(Constants.SHORT_ARG_PREFIX + flagAnnotation.shortName())
                           );
                })
                .forEach(field -> {
                    try {
                        if (field.getType().isAssignableFrom(boolean.class)) {
                            field.setBoolean(clazz, true);
                        } else if (field.getType().isAssignableFrom(int.class)) {
                            field.setInt(clazz, 1);
                        } else {
                            throw new UnsupportedFlagTypeException(
                                    field.getType().getSimpleName() + "is not a supported flag type"
                            );
                        }
                    } catch (final IllegalAccessException e) {
                        logger.error("Exception occurred when setting flag: {}", field.getName());
                    }
                });
    }
}
