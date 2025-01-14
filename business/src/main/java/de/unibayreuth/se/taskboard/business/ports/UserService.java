package de.unibayreuth.se.taskboard.business.ports;

import de.unibayreuth.se.taskboard.business.domain.User;
import de.unibayreuth.se.taskboard.business.exceptions.DuplicateNameException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    //TODO: Add user service interface that the controller uses to interact with the business layer.
    //TODO: Implement the user service interface in the business layer, using the existing user persistence service.
    /**
     * Retrieve a user by ID.
     *
     * @param id the ID of the user
     * @return an Optional containing the UserDto if found, or empty otherwise
     */
    Optional<User> getUserById(UUID id);

    /**
     * Retrieve all users.
     *
     * @return a list of UserDto objects
     */
    List<User> getAllUsers();

    /**
     * Create a new user.
     *
     * @param user the user data transfer object
     * @return the created User
     */
    User createUser(User user) throws DuplicateNameException;
}

