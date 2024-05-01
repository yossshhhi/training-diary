package kz.yossshhhi.dto;

public record AggregateWorkoutDataDTO(
    Long workoutCount,
    Long totalDuration,
    Long totalBurnedCalories
) {
}
