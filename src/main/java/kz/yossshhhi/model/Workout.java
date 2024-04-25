package kz.yossshhhi.model;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Workout {
    /**
     * The unique identifier of the workout.
     */
    private Long id;

    /**
     * The identifier of the user associated with this workout.
     */
    private Long userId;

    /**
     * The ID of the workout type associated with this workout.
     */
    private Long workoutTypeId;

    /**
     * The date when the workout was created.
     */
    private LocalDate createdAt;

    /**
     * The duration of the workout in minutes.
     */
    private Integer duration;

    /**
     * The number of calories burned during the workout.
     */
    private Integer burnedCalories;

    /**
     * A list containing extra options associated with the workout.
     * For example, extra options could include distance covered, speed, etc.
     */
    private List<ExtraOption> extraOptions;
}
