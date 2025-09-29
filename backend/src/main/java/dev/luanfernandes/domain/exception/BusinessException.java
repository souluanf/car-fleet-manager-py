package dev.luanfernandes.domain.exception;

import java.io.Serial;

public abstract class BusinessException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 6098402741363463495L;

    protected BusinessException(String message) {
        super(message);
    }

    public abstract int getHttpStatusCode();
}
