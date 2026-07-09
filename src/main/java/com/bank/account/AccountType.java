package com.bank.account;

/*
helper enum to manage the account type
CHECKING => means that the user has a checking account
SAVINGS => means that the user has a savings account
INVESTMENT => means that the user has an investment account

Note: any given user can have as many of these accounts as they want
@OneToMany relationship. there is one account id that can have as many unique
account types as they want
 */

public enum AccountType {
    CHECKING,
    SAVINGS,
    // no investment account file exists right now but its here in case i want to add it
    // TODO: add investment account file and update account service to reflect the added
    // TODO: account type
    INVESTMENT,
}
