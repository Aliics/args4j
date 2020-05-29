package fish.eyebrow.args4j.annotations;

import fish.eyebrow.args4j.Constants;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Flag {
    char shortName() default Constants.SHORT_NAME_DEFAULT;

    String description() default Constants.DESCRIPTION_DEFAULT;
}
