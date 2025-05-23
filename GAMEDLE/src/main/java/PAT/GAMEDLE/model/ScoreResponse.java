package PAT.GAMEDLE.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NegativeOrZero;
import jakarta.validation.constraints.NotBlank;

public record ScoreResponse(
        @NotBlank
        String user,

        @NotBlank
        String gamename,

        @Min(1)
        Integer attempts,

        Boolean success_wordle
) {
}