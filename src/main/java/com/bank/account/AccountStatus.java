package com.bank.account;

/*
helper enum to manage the status of an account regardless of specific account type
ACTIVE => means that the account is currently in use
FROZEN => means that the account has been frozen for some reason, no actions are allowed
CLOSED => means that the account is no longer active or has been manually removed for some
reason
*/

public enum AccountStatus {
    ACTIVE,
    FROZEN,
    CLOSED
}

