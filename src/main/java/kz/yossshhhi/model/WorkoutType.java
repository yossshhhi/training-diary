package kz.yossshhhi.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class WorkoutType {
    /**
     * The unique identifier for the workout type.
     */
    Long id;

    /**
     * The name of the workout type.
     */
    String name;
}
