package kz.yossshhhi.model;

import kz.yossshhhi.model.enums.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Represents a user in the system.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class User {
    /**
     * The unique identifier of the user.
     */
    Long id;

    /**
     * The username of the user.
     */
    String username;

    /**
     * The password of the user.
     */
    String password;

    /**
     * The role of the user.
     */
    Role role;
}
