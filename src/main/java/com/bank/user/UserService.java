package com.bank.user;

import com.bank.exception.UserNotFoundException;
import com.bank.repository.UserRepository;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public @NotNull User getUserByID(UUID userId) throws UserNotFoundException {
        return requireUserExists(userId);
    }


    public @NotNull User getUserByUsername(String username) throws UserNotFoundException {
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
    }
    // role not updatable: updates for admins only
    public @NotNull User updateUserProfile(UUID userId, String firstName, String lastName, Character middleInitial) throws UserNotFoundException {
        User user = requireUserExists(userId);
        // only sets a param if value is not null at last check
        if (firstName != null) { user.setFirstName(firstName); }
        if (middleInitial != null) { user.setMiddleInitial(middleInitial); }
        if (lastName != null) { user.setLastName(lastName); }
        userRepository.saveUser(user);
        return user;
    }

    public @NotNull List<User> getAllUsers() {
        return userRepository.findAllUsers();
    }

    private User requireUserExists(UUID userId) throws UserNotFoundException {
        return userRepository.findUserById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }
}
