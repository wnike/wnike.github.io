package com.ning.bishe01.service.ex;

public class PasswordnoMatchException extends ServiceException{
    public PasswordnoMatchException() {
    }

    public PasswordnoMatchException(String message) {
        super(message);
    }

    public PasswordnoMatchException(String message, Throwable cause) {
        super(message, cause);
    }

    public PasswordnoMatchException(Throwable cause) {
        super(cause);
    }

    public PasswordnoMatchException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
