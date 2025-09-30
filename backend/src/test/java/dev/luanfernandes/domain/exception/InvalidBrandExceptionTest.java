package dev.luanfernandes.domain.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class InvalidBrandExceptionTest {

    @Test
    void shouldCreateExceptionWithCorrectMessage() {
        var marca = "Toyota";
        var exception = new InvalidBrandException(marca);

        assertThat(exception.getMessage())
                .isEqualTo("Marca inválida: 'Toyota'. Consulte as marcas em /api/v1/veiculos/brands");
    }

    @Test
    void shouldReturnCorrectHttpStatusCode() {
        var exception = new InvalidBrandException("Honda");

        assertThat(exception.getHttpStatusCode()).isEqualTo(400);
    }

    @Test
    void shouldBeInstanceOfBusinessException() {
        var exception = new InvalidBrandException("Nissan");

        assertThat(exception).isInstanceOf(BusinessException.class);
    }
}
