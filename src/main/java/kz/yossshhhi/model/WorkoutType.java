package kz.yossshhhi.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkoutType {
    /**
     * The unique identifier for the workout type.
     */
    private Long id;

    /**
     * The name of the workout type.
     */
    private String name;
}
