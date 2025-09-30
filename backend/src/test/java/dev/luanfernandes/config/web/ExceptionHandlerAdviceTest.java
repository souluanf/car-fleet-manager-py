package dev.luanfernandes.config.web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.HttpStatus.*;

import dev.luanfernandes.domain.exception.VehicleNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class ExceptionHandlerAdviceTest {
    @InjectMocks
    private ExceptionHandlerAdvice exceptionHandlerAdvice;

    @Test
    @DisplayName("Should return a bad request when handle a MethodArgumentNotValidException")
    void handleMethodArgumentNotValidException() {
        MethodParameter methodParameter = new MethodParameter(this.getClass().getDeclaredMethods()[0], -1);
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "objectName");
        bindingResult.addError(new FieldError("objectName", "field", "defaultMessage"));

        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(methodParameter, bindingResult);

        ProblemDetail problemDetail = exceptionHandlerAdvice.handleMethodArgumentNotValidException(exception);

        assertNotNull(problemDetail);
        assertEquals(BAD_REQUEST.value(), problemDetail.getStatus());
    }

    @Test
    @DisplayName("Should return the correct status and detail when handling a ResponseStatusException")
    void handleResponseStatusException() {
        var status = NOT_FOUND;
        var reason = "Resource not found";
        var exception = new ResponseStatusException(status, reason);

        var responseEntity = exceptionHandlerAdvice.handleResponseStatusException(exception);

        assertNotNull(responseEntity);
        assertEquals(status, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(reason, responseEntity.getBody().getDetail());
    }

    @Test
    @DisplayName("Should return not found status when handling EntityNotFoundException")
    void handleEntityNotFoundException() {
        var message = "Entity not found";
        var exception = new EntityNotFoundException(message);

        var problemDetail = exceptionHandlerAdvice.handleEntityNotFoundException(exception);

        assertNotNull(problemDetail);
        assertEquals(NOT_FOUND.value(), problemDetail.getStatus());
        assertEquals(message, problemDetail.getDetail());
    }

    @Test
    @DisplayName("Should return custom status when handling BusinessException")
    void handleBusinessException() {
        var id = UUID.randomUUID();
        var exception = new VehicleNotFoundException(id);

        var responseEntity = exceptionHandlerAdvice.handleBusinessException(exception);

        assertNotNull(responseEntity);
        assertEquals(NOT_FOUND, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(
                "Veículo não encontrado com ID: " + id, responseEntity.getBody().getDetail());
        assertNotNull(responseEntity.getBody().getProperties().get("timestamp"));
    }

    @Test
    @DisplayName("Should return bad request when handling HttpMessageNotReadableException")
    void handleHttpMessageNotReadableException() {
        var exception = new HttpMessageNotReadableException("Invalid JSON", null, null);

        var problemDetail = exceptionHandlerAdvice.handleHttpMessageNotReadableException(exception);

        assertNotNull(problemDetail);
        assertEquals(BAD_REQUEST.value(), problemDetail.getStatus());
        assertEquals("Malformed JSON request", problemDetail.getDetail());
        assertNotNull(problemDetail.getProperties().get("timestamp"));
        assertNotNull(problemDetail.getProperties().get("detail"));
    }

    @Test
    @DisplayName("Should return internal server error when handling InvalidDataAccessResourceUsageException")
    void handleInvalidDataAccessResourceUsageException() {
        var message = "Invalid SQL query";
        var exception = new InvalidDataAccessResourceUsageException(message);

        var problemDetail = exceptionHandlerAdvice.handleInvalidDataAccessResourceUsageException(exception);

        assertNotNull(problemDetail);
        assertEquals(INTERNAL_SERVER_ERROR.value(), problemDetail.getStatus());
        assertEquals(message, problemDetail.getDetail());
        assertNotNull(problemDetail.getProperties().get("timestamp"));
    }

    @Test
    @DisplayName("Should return service unavailable when handling ResourceAccessException")
    void handleResourceAccessException() {
        var message = "Service unavailable";
        var exception = new ResourceAccessException(message);

        var problemDetail = exceptionHandlerAdvice.handleHttpClientErrorExceptionNotFound(exception);

        assertNotNull(problemDetail);
        assertEquals(SERVICE_UNAVAILABLE.value(), problemDetail.getStatus());
        assertEquals(message, problemDetail.getDetail());
        assertNotNull(problemDetail.getProperties().get("timestamp"));
    }

    @Test
    @DisplayName("Should return bad request when handling MethodArgumentTypeMismatchException")
    void handleMethodArgumentTypeMismatchException() {
        var exception = new MethodArgumentTypeMismatchException(
                "invalid", UUID.class, "id", null, new IllegalArgumentException("Invalid UUID"));

        var problemDetail = exceptionHandlerAdvice.handleMethodArgumentTypeMismatchException(exception);

        assertNotNull(problemDetail);
        assertEquals(BAD_REQUEST.value(), problemDetail.getStatus());
        assertNotNull(problemDetail.getDetail());
        assertNotNull(problemDetail.getProperties().get("timestamp"));
    }

    @Test
    @DisplayName("Should return bad request when handling ConstraintViolationException")
    void handleConstraintViolationException() {
        var violation = new ConstraintViolation<Object>() {
            @Override
            public String getMessage() {
                return "must not be null";
            }

            @Override
            public String getMessageTemplate() {
                return null;
            }

            @Override
            public Object getRootBean() {
                return null;
            }

            @Override
            public Class<Object> getRootBeanClass() {
                return null;
            }

            @Override
            public Object getLeafBean() {
                return null;
            }

            @Override
            public Object[] getExecutableParameters() {
                return new Object[0];
            }

            @Override
            public Object getExecutableReturnValue() {
                return null;
            }

            @Override
            public jakarta.validation.Path getPropertyPath() {
                return null;
            }

            @Override
            public Object getInvalidValue() {
                return null;
            }

            @Override
            public jakarta.validation.metadata.ConstraintDescriptor<?> getConstraintDescriptor() {
                return null;
            }

            @Override
            public <U> U unwrap(Class<U> type) {
                return null;
            }
        };

        var exception = new ConstraintViolationException(Set.of(violation));

        var responseEntity = exceptionHandlerAdvice.handleConstraintViolationException(exception);

        assertNotNull(responseEntity);
        assertEquals(BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("must not be null", responseEntity.getBody().getDetail());
        assertNotNull(responseEntity.getBody().getProperties().get("timestamp"));
        assertNotNull(responseEntity.getBody().getProperties().get("violations"));
    }

    @Test
    @DisplayName("Should return internal server error when handling generic Exception")
    void handleGenericException() {
        var message = "Unexpected error";
        var exception = new RuntimeException(message);

        var problemDetail = exceptionHandlerAdvice.handleGenericException(exception);

        assertNotNull(problemDetail);
        assertEquals(INTERNAL_SERVER_ERROR.value(), problemDetail.getStatus());
        assertEquals(message, problemDetail.getDetail());
        assertNotNull(problemDetail.getProperties().get("timestamp"));
    }

    @Test
    @DisplayName("Should handle MethodArgumentNotValidException with object errors")
    void handleMethodArgumentNotValidExceptionWithObjectErrors() {
        MethodParameter methodParameter = new MethodParameter(this.getClass().getDeclaredMethods()[0], -1);
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "objectName");
        bindingResult.addError(new ObjectError("objectName", "Object validation failed"));

        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(methodParameter, bindingResult);

        ProblemDetail problemDetail = exceptionHandlerAdvice.handleMethodArgumentNotValidException(exception);

        assertNotNull(problemDetail);
        assertEquals(BAD_REQUEST.value(), problemDetail.getStatus());
        assertEquals("Object validation failed", problemDetail.getDetail());
        assertNotNull(problemDetail.getProperties().get("violations"));
    }

    @Test
    @DisplayName("Should handle ConstraintViolationException with empty violations")
    void handleConstraintViolationExceptionWithEmptyViolations() {
        var exception = new ConstraintViolationException(Set.of());

        var responseEntity = exceptionHandlerAdvice.handleConstraintViolationException(exception);

        assertNotNull(responseEntity);
        assertEquals(BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("Validation failed", responseEntity.getBody().getDetail());
    }
}
