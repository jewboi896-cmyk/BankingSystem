package com.bank.api.dto.response;

import com.bank.role.Role;
import com.bank.user.User;

import java.util.UUID;

public record UserResponse(UUID userID, String username, Role role) {
    /**
     * @author Derek Homel
     * @summary
     * @param user user being referenced
     * @return returns a user response that consists of the userID, username,
     * and user role
     */
    public static UserResponse fromUser(User user) {
        return new UserResponse(user.getUserID(), user.getUsername(),
                user.getRole());
    }
}
