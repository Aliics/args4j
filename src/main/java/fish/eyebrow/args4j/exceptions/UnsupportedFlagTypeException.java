package fish.eyebrow.args4j.exceptions;

public class UnsupportedFlagTypeException extends RuntimeException {
    public UnsupportedFlagTypeException() {
    }

    public UnsupportedFlagTypeException(final String message) {
        super(message);
    }

    public UnsupportedFlagTypeException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public UnsupportedFlagTypeException(final Throwable cause) {
        super(cause);
    }

    public UnsupportedFlagTypeException(
            final String message,
            final Throwable cause,
            final boolean enableSuppression,
            final boolean writableStackTrace
    ) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
