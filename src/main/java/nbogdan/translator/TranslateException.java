package nbogdan.translator;

public class TranslateException extends Exception {
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
