package PAT.GAMEDLE.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class WordleResponseUnitTest {

    private static Validator validator;

    @BeforeAll
    static void setupValidator() {
        validator = Validation
                .buildDefaultValidatorFactory()
                .getValidator();
    }

    @Test
    void testValidWordleResponse() {
        WordleResponse resp = new WordleResponse("APPLE");
        Set<ConstraintViolation<WordleResponse>> violations =
                validator.validate(resp);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testBlankWord() {
        WordleResponse resp = new WordleResponse("");
        Set<ConstraintViolation<WordleResponse>> violations =
                validator.validate(resp);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("word")));
    }

    @Test
    void testTooShortWord() {
        WordleResponse resp = new WordleResponse("CAT");
        Set<ConstraintViolation<WordleResponse>> violations =
                validator.validate(resp);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("5 letras")));
    }

    @Test
    void testTooLongWord() {
        WordleResponse resp = new WordleResponse("TOOLONG");
        Set<ConstraintViolation<WordleResponse>> violations =
                validator.validate(resp);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("5 letras")));
    }
}
