package com.bank.api.dto.request;

import com.bank.role.Role;

public record RegisterRequest(String username, String firstName,
                              String lastName, Character middleInitial,
                              String password, Role role) {}