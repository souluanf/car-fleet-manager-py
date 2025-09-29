package dev.luanfernandes.domain.exception;

import java.io.Serial;

public class InvalidBrandException extends BusinessException {

    @Serial
    private static final long serialVersionUID = 1L;

    public InvalidBrandException(String marca) {
        super("Marca inválida: '" + marca + "'. Consulte as marcas em /api/v1/veiculos/brands");
    }

    @Override
    public int getHttpStatusCode() {
        return 400;
    }
}
