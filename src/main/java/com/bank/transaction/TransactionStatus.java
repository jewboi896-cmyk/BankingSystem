package com.bank.transaction;

/*
helper enum to manage the stages of a transaction
PENDING => means that the transaction has yet to be approved and no money has been moved
COMPLETED => means that the transaction has been completed and the money has been moved
FAILED => means that for whatever reason, the transaction couldn't be completed
REVERSED => means the transaction has be reverted to a state before the transaction was
requested
 */

public enum TransactionStatus {
    PENDING,
    COMPLETED,
    FAILED,
    REVERSED
}
