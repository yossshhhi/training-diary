package kz.yossshhhi.model;

import lombok.*;

/**
 * Represents an extra option type for a workout.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExtraOptionType {
    /**
     * The unique identifier of the extra option type.
     */
    private Long id;

    /**
     * The name of the extra option type.
     */
    private String name;
}

