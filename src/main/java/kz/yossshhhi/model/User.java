package kz.yossshhhi.model;

import kz.yossshhhi.model.enums.Role;
import kz.yossshhhi.starter.audit.aop.model.Identifiable;
import lombok.*;

/**
 * Represents a user in the system.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements Identifiable {
    /**
     * The unique identifier of the user.
     */
    private Long id;

    /**
     * The username of the user.
     */
    private String username;

    /**
     * The password of the user.
     */
    private String password;

    /**
     * The role of the user.
     */
    private Role role;
}
