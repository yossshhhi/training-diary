package kz.yossshhhi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import kz.yossshhhi.starter.audit.aop.model.AuthenticatedRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthenticationDTO implements AuthenticatedRequest {
    @NotBlank(message = "Username must not be blank")
    @Size(min = 3, max = 50, message = "Username length must be between 3 and 50")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Username must consist of letters and numbers only")
    private String username;
    @NotBlank(message = "Password must not be blank")
    @Size(min = 5, max = 50, message = "Password length must be between 5 and 50")
    private String password;
}
