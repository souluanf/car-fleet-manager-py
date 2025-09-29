package dev.luanfernandes.domain.exception;

import java.io.Serial;

public class InvalidVotingDataException extends BusinessException {

    @Serial
    private static final long serialVersionUID = 1L;

    public InvalidVotingDataException(String message) {
        super(message);
    }

    @Override
    public int getHttpStatusCode() {
        return 400;
    }
}
