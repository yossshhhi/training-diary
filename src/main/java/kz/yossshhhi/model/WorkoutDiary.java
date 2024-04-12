package kz.yossshhhi.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

/**
 * Represents a workout diary containing a list of workouts.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class WorkoutDiary {
    /**
     * The unique identifier of the workout diary.
     */
    Long id;

    /**
     * The user ID associated with the workout diary.
     */
    Long userId;

    /**
     * The list of workouts recorded in the diary.
     */
    List<Workout> workoutList;
}
