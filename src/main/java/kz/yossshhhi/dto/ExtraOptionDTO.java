package kz.yossshhhi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record ExtraOptionDTO(
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    Long typeId,
    String typeName,
    @NotBlank
    Integer value
) {
}
