package projectbusan.gongda.exception;

public class AccessPermissionException extends RuntimeException{
    public AccessPermissionException() {
    }

    public AccessPermissionException(String message) {
        super(message);
    }

    public AccessPermissionException(String message, Throwable cause) {
        super(message, cause);
    }

    public AccessPermissionException(Throwable cause) {
        super(cause);
    }

    public AccessPermissionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
