package com.web.doitcommit.handler.exception;

public class NoSavedDataException extends RuntimeException{

    private static final long serialVersionUID=1L;

    public NoSavedDataException(String message) {
        super(message);
    }

}
