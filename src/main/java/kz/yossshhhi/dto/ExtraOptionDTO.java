package kz.yossshhhi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ExtraOptionDTO(
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    Long typeId,
    String typeName,
    Integer value
) {
}
