package com.bank.repository.json;

import com.bank.file.FileHelper;
import com.bank.repository.UserRepository;
import com.bank.user.User;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class JsonUserRepository implements UserRepository {
    private final Map<UUID, User> usersById;
    private final Map<String, User> usersByUsername;
    private final FileHelper<User> fileHelper;

    public JsonUserRepository(@NotNull FileHelper<User> fileHelper) {
        this.fileHelper = fileHelper;
        this.usersById = fileHelper.loadFromFile();
        this.usersByUsername = buildUsernameIndex();
    }

    private @NotNull Map<String, User> buildUsernameIndex() {
        Map<String, User> index = new HashMap<>();
        for (User user : usersById.values()) {
            index.put(user.getUsername(), user);
        }
        return index;
    }

    @Override
    public void saveUser(User user) {
        usersById.put(user.getUserID(), user);
        usersByUsername.put(user.getUsername(), user);
        fileHelper.writeToFile(usersById);
    }

    @Override
    public @NotNull Optional<User> findUserById(UUID userId) {
        return Optional.ofNullable(usersById.get(userId));
    }

    @Override
    public @NotNull Optional<User> findUserByUsername(String username) {
        return Optional.ofNullable(usersByUsername.get(username));
    }

    @Override
    public List<User> findAllUsers() {
        if (usersById == null) {
            return Collections.emptyList();
        }
        return new ArrayList<>(usersById.values());
    }

    @Override
    public void deleteUser(UUID userId) {
        User user = usersById.remove(userId);
        if (user != null) {
            usersByUsername.remove(user.getUsername());
        }
        fileHelper.writeToFile(usersById);
    }
}
