package de.unibayreuth.se.taskboard.business.impl;

import de.unibayreuth.se.taskboard.business.domain.User;
import de.unibayreuth.se.taskboard.business.exceptions.DuplicateNameException;
import de.unibayreuth.se.taskboard.business.ports.UserService;
import de.unibayreuth.se.taskboard.business.ports.UserPersistenceService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementation of the UserService interface using the UserPersistenceService.
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserPersistenceService persistenceService;

    public UserServiceImpl(UserPersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    @Override
    public Optional<User> getUserById(UUID id) {
        return persistenceService.getById(id);
    }

    @Override
    public List<User> getAllUsers() {
        return persistenceService.getAll();
    }

    @Override
    public User createUser(User user) throws DuplicateNameException {
        // create new user object
        User newUser = new User(user.getName());
        // save user with persistenceService
        User savedUser = persistenceService.upsert(newUser);
        // return user
        return savedUser;
    }
}
