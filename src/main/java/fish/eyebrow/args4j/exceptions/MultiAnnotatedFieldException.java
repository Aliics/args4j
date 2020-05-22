package fish.eyebrow.args4j.exceptions;

public class MultiAnnotatedFieldException extends RuntimeException {
    public MultiAnnotatedFieldException() {
    }

    public MultiAnnotatedFieldException(final String message) {
        super(message);
    }

    public MultiAnnotatedFieldException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public MultiAnnotatedFieldException(final Throwable cause) {
        super(cause);
    }

    public MultiAnnotatedFieldException(
            final String message,
            final Throwable cause,
            final boolean enableSuppression,
            final boolean writableStackTrace
    ) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
