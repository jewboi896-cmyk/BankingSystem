package com.bank.api.dto.response;

import com.bank.role.Role;
import com.bank.user.User;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record UserResponse(UUID userID, String username, Role role) {
    /**
     * @author Derek Homel
     * @summary
     * @param user user being referenced
     * @return returns a user response that consists of the userID, username,
     * and user role
     */
    @Contract("_ -> new")
    public static @NotNull UserResponse fromUser(@NotNull User user) {
        return new UserResponse(user.getUserID(), user.getUsername(),
                user.getRole());
    }
}
