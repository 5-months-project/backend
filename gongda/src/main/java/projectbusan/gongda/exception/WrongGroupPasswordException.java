package projectbusan.gongda.exception;

public class WrongGroupPasswordException extends RuntimeException {
    public WrongGroupPasswordException() {
    }

    public WrongGroupPasswordException(String message) {
        super(message);
    }

    public WrongGroupPasswordException(String message, Throwable cause) {
        super(message, cause);
    }

    public WrongGroupPasswordException(Throwable cause) {
        super(cause);
    }

    public WrongGroupPasswordException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
