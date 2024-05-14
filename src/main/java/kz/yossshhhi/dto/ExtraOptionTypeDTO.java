package kz.yossshhhi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ExtraOptionTypeDTO(
        Long id,
        @NotBlank
        @Pattern(regexp = "^[a-zA-Z 0-9]+$", message = "Type name must consist of letters and numbers only")
        String name
) {
}
