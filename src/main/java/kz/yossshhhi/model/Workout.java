package kz.yossshhhi.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class Workout {
    /**
     * The unique identifier of the workout.
     */
    Long id;

    /**
     * The ID of the workout type associated with this workout.
     */
    Long workoutTypeId;

    /**
     * The date when the workout was created.
     */
    LocalDate createdAt;

    /**
     * The duration of the workout in minutes.
     */
    Integer duration;

    /**
     * The number of calories burned during the workout.
     */
    Integer burnedCalories;

    /**
     * A map containing extra options associated with the workout and their corresponding values.
     * For example, extra options could include distance covered, speed, etc.
     */
    Map<ExtraOption, Integer> extraOptions;
}
