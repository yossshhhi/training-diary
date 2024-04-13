package kz.yossshhhi.model;

import lombok.*;

/**
 * Represents an extra option for a workout.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExtraOption {
    /**
     * The unique identifier of the extra option.
     */
    private Long id;

    /**
     * The name of the extra option.
     */
    private String name;
}

