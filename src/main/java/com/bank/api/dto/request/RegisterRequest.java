package com.bank.api.dto.request;

public record RegisterRequest(String username, String firstName,
                              String lastName, Character middleInitial,
                              String password) {}