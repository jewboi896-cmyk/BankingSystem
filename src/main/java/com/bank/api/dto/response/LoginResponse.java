package com.bank.api.dto.response;

import com.bank.role.Role;
import java.util.UUID;

public record LoginResponse(String token, UUID userID,
                            String username, Role role,
                            int expiryTimeInSeconds) {
}
