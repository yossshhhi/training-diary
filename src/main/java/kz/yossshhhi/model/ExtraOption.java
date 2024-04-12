package kz.yossshhhi.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Represents an extra option for a workout.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ExtraOption {
    /**
     * The unique identifier of the extra option.
     */
    Long id;

    /**
     * The name of the extra option.
     */
    String name;
}

