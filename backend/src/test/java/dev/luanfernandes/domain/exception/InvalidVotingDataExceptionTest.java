package dev.luanfernandes.domain.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class InvalidVotingDataExceptionTest {

    @Test
    void shouldCreateExceptionWithCorrectMessage() {
        var message = "Dados de votação inválidos";
        var exception = new InvalidVotingDataException(message);

        assertThat(exception.getMessage()).isEqualTo(message);
    }

    @Test
    void shouldReturnCorrectHttpStatusCode() {
        var exception = new InvalidVotingDataException("Erro");

        assertThat(exception.getHttpStatusCode()).isEqualTo(400);
    }

    @Test
    void shouldBeInstanceOfBusinessException() {
        var exception = new InvalidVotingDataException("Teste");

        assertThat(exception).isInstanceOf(BusinessException.class);
    }
}
