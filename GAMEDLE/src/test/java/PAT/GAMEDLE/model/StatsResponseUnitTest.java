package PAT.GAMEDLE.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class StatsResponseUnitTest {

    private static Validator validator;

    @BeforeAll
    static void setupValidator() {
        validator = Validation
                .buildDefaultValidatorFactory()
                .getValidator();
    }

    @Test
    void testValidStatsResponse() {
        StatsResponse stats = new StatsResponse(
                "player1",
                "wordle",
                2.5f,
                80f
        );
        Set<ConstraintViolation<StatsResponse>> violations =
                validator.validate(stats);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testBlankUser() {
        StatsResponse stats = new StatsResponse(
                "",          // blank user
                "wordle",
                1f,
                50f
        );
        Set<ConstraintViolation<StatsResponse>> violations =
                validator.validate(stats);
        assertFalse(violations.isEmpty());
        // debería contener al menos una violación en el field "user"
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("user")));
    }

    @Test
    void testBlankGameName() {
        StatsResponse stats = new StatsResponse(
                "player1",
                "   ",       // blank gamename
                1f,
                50f
        );
        Set<ConstraintViolation<StatsResponse>> violations =
                validator.validate(stats);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("gamename")));
    }

    @Test
    void testNegativeAttempts() {
        StatsResponse stats = new StatsResponse(
                "player1",
                "wordle",
                -1f,         // invalid negative
                50f
        );
        Set<ConstraintViolation<StatsResponse>> violations =
                validator.validate(stats);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("attempts_pg")));
    }

    @Test
    void testNegativeSuccessRate() {
        StatsResponse stats = new StatsResponse(
                "player1",
                "wordle",
                3f,
                -10f         // invalid negative
        );
        Set<ConstraintViolation<StatsResponse>> violations =
                validator.validate(stats);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("success_rate")));
    }
}
