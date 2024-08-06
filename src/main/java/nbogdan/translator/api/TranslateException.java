package nbogdan.translator.api;

public class TranslateException extends RuntimeException {
    public TranslateException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public TranslateException(final Throwable cause) {
        super(cause);
    }

    public TranslateException(final String message) {
        super(message);
    }
}
