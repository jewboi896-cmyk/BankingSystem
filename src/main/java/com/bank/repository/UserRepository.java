package com.bank.repository;

import com.bank.user.User;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    void saveUser(User user);
    @NotNull Optional<User> findUserById(UUID userId);
    @NotNull Optional<User> findUserByUsername(String username);
    List<User> findAllUsers();
    void deleteUser(UUID userId);
}
