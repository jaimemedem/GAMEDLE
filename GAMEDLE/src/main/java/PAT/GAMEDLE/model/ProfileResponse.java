package PAT.GAMEDLE.model;

public record ProfileResponse(
        String name,
        String email,
        Role role
) { }
