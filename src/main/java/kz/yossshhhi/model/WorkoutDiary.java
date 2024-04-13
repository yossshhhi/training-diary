package kz.yossshhhi.model;

import lombok.*;

import java.util.List;

/**
 * Represents a workout diary containing a list of workouts.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkoutDiary {
    /**
     * The unique identifier of the workout diary.
     */
    private Long id;

    /**
     * The user ID associated with the workout diary.
     */
    private Long userId;

    /**
     * The list of workouts recorded in the diary.
     */
    private List<Workout> workoutList;
}
