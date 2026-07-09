package com.bank.repository.inMem;

import com.bank.repository.UserRepository;
import com.bank.user.User;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class InMemoryUserRepository implements UserRepository {
    private final Map<UUID, User> usersById = new HashMap<>();
    private final Map<String, User> usersByUsername = new HashMap<>();

    /**
     * @author Derek Homel
     * @summary
     * @param user the user to save
     */
    @Override
    public void saveUser(User user) {
        usersById.put(user.getUserID(), user);
        usersByUsername.put(user.getUsername(), user);
    }

    /**
     * @author Derek Homel
     * @summary
     * @param userId the userID to find
     * @return returns an Optional of the userID to ensure never null
     */
    @Override
    public @NotNull Optional<User> findUserById(UUID userId) {
        return Optional.ofNullable(usersById.get(userId));
    }

    /**
     * @author Derek Homel
     * @summary
     * @param username the username to find the user with
     * @return returns an Optional of the users username to ensure never null
     */
    @Override
    public @NotNull Optional<User> findUserByUsername(String username) {
        return Optional.ofNullable(usersByUsername.get(username));
    }

    /**
     * @author Derek Homel
     * @summary
     * @return returns a Collection all users by ID
     */
    @Override
    public @NotNull List<User> findAllUsers() {
        return new ArrayList<>(usersById.values());
    }

    /**
     * @author Derek Homel
     * @summary
     * @param userId the userID to remove
     */
    @Override
    public void deleteUser(UUID userId) {
        User user = usersById.remove(userId);
        if (user != null) {
            usersByUsername.remove(user.getUsername());
        }
        System.out.println("User does not exist");
    }
}
