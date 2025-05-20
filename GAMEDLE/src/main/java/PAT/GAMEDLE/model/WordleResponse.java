package PAT.GAMEDLE.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record WordleResponse(

    @NotBlank
    @Size(min = 5 , max = 5, message ="la palabra tiene que tener 5 letras")
    String word
){}
