package kz.yossshhhi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AuthenticationDTO (
        @NotBlank(message = "Username must not be blank")
        @Size(min = 3, max = 50, message = "Username length must be between 3 and 50")
        @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Username must consist of letters and numbers only")
        String username,
        @NotBlank(message = "Password must not be blank")
        @Size(min = 5, max = 50, message = "Password length must be between 5 and 50")
        String password
) {}
