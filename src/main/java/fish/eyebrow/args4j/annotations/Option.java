package fish.eyebrow.args4j.annotations;

import fish.eyebrow.args4j.Constants;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Command line option for passing in variables for the program's runtime.
 * In the currently implementation, these should be String types.
 * <p>
 * When passed in as an argument, this should be matched against a word
 * beginning with two hyphens (--), no space, and some characters. This is
 * terminated by a space.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Option {
    /**
     * Another way to pass the option in is using this short name.
     * As an argument they are denoted with a single hyphen (-) and
     * a single character, typically alphanumeric.
     */
    char shortName() default Constants.SHORT_NAME_DEFAULT;

    /**
     * Description of the option's usage for the program's help text.
     */
    String description() default Constants.DESCRIPTION_DEFAULT;
}
