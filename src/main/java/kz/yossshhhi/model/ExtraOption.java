package kz.yossshhhi.model;

import lombok.*;

/**
 * Represents an extra option associated with a workout.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class ExtraOption {
    /**
     * The unique identifier of the extra option.
     */
    private Long id;

    /**
     * The identifier of the workout associated with this extra option.
     */
    private Long workoutId;

    /**
     * The identifier of the type of extra option.
     */
    private Long typeId;

    /**
     * The value of the extra option.
     */
    private Integer value;
}

