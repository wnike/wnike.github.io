package com.ning.bishe01.service.ex;

public class RolenameExistException extends ServiceException{
    public RolenameExistException() {
    }

    public RolenameExistException(String message) {
        super(message);
    }

    public RolenameExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public RolenameExistException(Throwable cause) {
        super(cause);
    }

    public RolenameExistException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
