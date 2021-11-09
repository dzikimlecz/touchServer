package me.dzikimlecz.touchserver.model;

public class ElementAlreadyExistException extends RuntimeException {
    public ElementAlreadyExistException() {
        super();
    }

    public ElementAlreadyExistException(String message) {
        super(message);
    }

    public ElementAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public ElementAlreadyExistException(Throwable cause) {
        super(cause);
    }

    protected ElementAlreadyExistException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
