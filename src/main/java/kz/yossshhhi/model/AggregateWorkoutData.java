package kz.yossshhhi.model;

import lombok.*;

/**
 * A data class representing aggregate workout statistics.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AggregateWorkoutData {
    /**
     * The total number of workouts.
     */
    private Long workoutCount;

    /**
     * The total duration of all workouts in minutes.
     */
    private Long totalDuration;

    /**
     * The total number of calories burned in all workouts.
     */
    private Long totalBurnedCalories;
}

