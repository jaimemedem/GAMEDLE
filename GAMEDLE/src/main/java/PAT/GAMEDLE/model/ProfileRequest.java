package PAT.GAMEDLE.model;

import jakarta.validation.constraints.Pattern;

public record ProfileRequest(
        String name,
        Role role,
        // Patrón: al menos una mayúscula, una minúscula, y un número, y de longitud más de 7
        @Pattern(regexp = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[a-z]).{8,}$")
        String password
) {
    @Override
    public Role role() {
        return role;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String password() {
        return password;
    }
}