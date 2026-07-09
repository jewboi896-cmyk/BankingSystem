package com.bank.transaction;

/*
helper enum to manage what track what type of transaction has been completed
DEPOSIT => means the user is adding money to one or more of their accounts
WITHDRAW => means the user has removed money from one or more of their accounts
TRANSFER => means the user has moved money from one account to another
FEE => means that the bank is charging the user for a service 
 */

public enum TransactionType {
    DEPOSIT,
    WITHDRAW,
    TRANSFER,
    FEE
}
