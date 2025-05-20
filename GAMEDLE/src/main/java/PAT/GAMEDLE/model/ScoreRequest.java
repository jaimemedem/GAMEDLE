package PAT.GAMEDLE.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NegativeOrZero;
import jakarta.validation.constraints.NotBlank;

public record ScoreRequest(

        @NotBlank
        String gamename,


        @Min(1)
        Integer attempts

) {
}
