package kz.yossshhhi.dao.repository;

import kz.yossshhhi.model.User;

import java.util.Optional;

/**
 * Interface representing a repository for managing user data.
 */
public interface UserRepository {

    /**
     * Finds a user by their unique identifier.
     *
     * @param id The unique identifier of the user.
     * @return An {@link Optional} containing the user if found, or empty if not found.
     */
    Optional<User> findById(Long id);

    /**
     * Finds a user by their username.
     *
     * @param username The username of the user.
     * @return An {@link Optional} containing the user if found, or empty if not found.
     */
    Optional<User> findByUsername(String username);

    /**
     * Saves a user.
     *
     * @param user The user to save.
     * @return The saved user.
     */
    User save(User user);
}

