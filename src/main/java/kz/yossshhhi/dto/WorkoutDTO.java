package kz.yossshhhi.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.List;

public record WorkoutDTO(
    Long id,
    String username,
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    Long workoutTypeId,
    String workoutTypeName,
    LocalDate createdAt,
    Integer duration,
    Integer burnedCalories,
    List<ExtraOptionDTO> extraOptions
) {
}
