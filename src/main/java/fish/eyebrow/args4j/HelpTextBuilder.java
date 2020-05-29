package fish.eyebrow.args4j;

import fish.eyebrow.args4j.annotations.Flag;
import fish.eyebrow.args4j.annotations.Option;

import java.lang.reflect.Field;
import java.util.Objects;

public class HelpTextBuilder {
    private StringBuilder helpTextStringBuilder;

    private HelpTextBuilder() {}

    static HelpTextBuilder ofDefaults() {
        final var helpTextBuilder = new HelpTextBuilder();
        helpTextBuilder.helpTextStringBuilder = new StringBuilder();

        return helpTextBuilder;
    }

    String buildHelpText() {
        return helpTextStringBuilder.toString();
    }

    void buildInitialHelpText() {
        helpTextStringBuilder.append(" ")
                .append(Constants.HELP_TEXT_PREFIX)
                .append("\n")
                .append("  ")
                .append(Constants.SHORT_ARG_PREFIX)
                .append("h, ")
                .append(Constants.LONG_ARG_PREFIX)
                .append("help")
                .append("\t")
                .append(Constants.HELP_TEXT_DESCRIPTION)
                .append("\n");
    }

    boolean canBuildHelpText(final Field field) {
        final var flagAnnotation = field.getAnnotation(Flag.class);
        final var optionAnnotation = field.getAnnotation(Option.class);

        if (Objects.nonNull(flagAnnotation)) {
            buildHelpTextForArgument(field, flagAnnotation.shortName(), flagAnnotation.description());
            return true;
        } else if (Objects.nonNull(optionAnnotation)) {
            buildHelpTextForArgument(field, optionAnnotation.shortName(), optionAnnotation.description());
            return true;
        }

        return false;
    }

    private void buildHelpTextForArgument(final Field field, final String s, final String description) {
        helpTextStringBuilder.append("  ");
        if (!s.equals("")) {
            helpTextStringBuilder.append(Constants.SHORT_ARG_PREFIX)
                    .append(s)
                    .append(", ");
        }

        helpTextStringBuilder.append(Constants.LONG_ARG_PREFIX)
                .append(field.getName())
                .append("\t")
                .append(description)
                .append("\n");
    }
}
