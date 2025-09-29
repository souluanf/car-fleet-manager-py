package dev.luanfernandes.domain.exception;

import java.io.Serial;
import java.util.UUID;

public class VehicleNotFoundException extends BusinessException {

    @Serial
    private static final long serialVersionUID = -2320003187939354195L;

    public VehicleNotFoundException(UUID id) {
        super("Veículo não encontrado com ID: " + id);
    }

    @Override
    public int getHttpStatusCode() {
        return 404;
    }
}
