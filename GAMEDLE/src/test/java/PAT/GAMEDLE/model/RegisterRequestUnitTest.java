package PAT.GAMEDLE.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RegisterRequestUnitTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    public void testValidRequest() {
        // Given ...
        RegisterRequest registro = new RegisterRequest(
                "Nombre", "nombre@email.com",
                Role.USER, "aaaaaaA1");
        // When ...
        Set<ConstraintViolation<RegisterRequest>> violations =
                validator.validate(registro);
        // Then ...
        assertTrue(violations.isEmpty());
    }


    @Test
    public void testEmptyPassword(){
        RegisterRequest registro = new RegisterRequest("nombre", "nombre@correo.es", Role.USER,"" );

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(registro);

        assertFalse(violations.isEmpty());
    }

    @Test
    public void testBadEmail(){
        RegisterRequest registro = new RegisterRequest("nombre", "nombrecorreomalo", Role.USER,"aaaaaaaA1" );

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(registro);

        assertFalse(violations.isEmpty());
    }



}