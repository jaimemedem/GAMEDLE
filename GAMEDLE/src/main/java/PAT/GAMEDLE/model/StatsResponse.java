package PAT.GAMEDLE.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record StatsResponse(
        @NotBlank
        String user,

        @NotBlank
        String gamename,

        @Min(0)
        Float attempts_pg,

        @Min(0)
        Float success_rate
) {
}
