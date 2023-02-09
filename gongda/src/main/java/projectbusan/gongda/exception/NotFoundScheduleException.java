package projectbusan.gongda.exception;

public class NotFoundScheduleException extends RuntimeException{

    public NotFoundScheduleException() {
    }

    public NotFoundScheduleException(String message) {
        super(message);
    }

    public NotFoundScheduleException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundScheduleException(Throwable cause) {
        super(cause);
    }

    public NotFoundScheduleException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
