package com.bank.transaction;

import com.bank.account.Account;
import com.bank.account.AccountService;
import com.bank.exception.AccountNotFoundException;
import com.bank.exception.BankingException;
import com.bank.exception.TransactionFailedException;
import com.bank.exception.TransactionNotFoundException;
import com.bank.repository.TransactionRepository;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static com.bank.transaction.TransactionStatus.COMPLETED;
import static com.bank.transaction.TransactionStatus.FAILED;
import static com.bank.transaction.TransactionType.*;

public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountService accountService;

    public TransactionService(TransactionRepository transactionRepository, AccountService accountService) {
        this.transactionRepository = transactionRepository;
        this.accountService = accountService;
    }

    /*
    method to deposit a transaction
    it first finds the users accountID from the accountRepo and throws an
    AccountNotFoundException if the ID doesn't exist. it then validates the deposit,
    creates a transaction,adds the transaction amount to the account with the ID,
    internally sets the status to complete, saves the transaction and the account
    and returns the transaction
     */
    @NotNull
    public Transaction depositFunds(UUID accountId, BigDecimal amount, String description) throws BankingException {
        Account account = accountService.getAccountByID(accountId);
        account.validateDeposit(amount);
        Transaction transaction = new Transaction(DEPOSIT, amount, null, accountId, description);
        try {
            accountService.applyDeposit(account, amount);
        } catch (Exception e) {
            transaction.setTransactionStatus(FAILED);
            transactionRepository.saveTransaction(transaction);
            throw new TransactionFailedException("Failed to deposit funds into account: " + account.getAccountID(), e);
        }
        transaction.setTransactionStatus(COMPLETED);
        transactionRepository.saveTransaction(transaction);
        return transaction;
    }

    /*
    method to withdraw money from an account
    finds the account ID from the account repo and throws an AccountNotFoundException if
    not found. it then validates the withdrawal, creates a transaction, subtracts it from the
    accounts balance. if the account type is a savings account, it increments the monthly
    withdrawal limit for that account, sets the status to complete, saves the transaction,
    saves the account, and returns the transaction
     */
    @NotNull
    public Transaction withdrawFunds(UUID accountID, BigDecimal amount, String description) throws BankingException {
        Account account = accountService.getAccountByID(accountID);

        account.validateWithdrawal(amount);
        Transaction transaction = new Transaction(WITHDRAW, amount, accountID, null, description);
        try {
            accountService.applyWithdrawal(account, amount);
        } catch (Exception e) {
            transaction.setTransactionStatus(FAILED);
            transactionRepository.saveTransaction(transaction);
            throw new TransactionFailedException("Failed to withdraw requested funds from account: " + account.getAccountID(), e);
        }
        transaction.setTransactionStatus(COMPLETED);
        transactionRepository.saveTransaction(transaction);
        return transaction;
    }
    /*
    method to transfer funds from on account to the next
    it first finds both the source and destination accounts from the account repo. it then
    validates the transfer using the amount and the destination account. it then validates
    the amount destination account receives. it then creates a TRANSFER transaction,
    subtracts the amount from the source account and adds it to the destination account. if
    the account is a savings account it increments the monthly withdrawal limit for that account.
    it then saves both the account(source and destination) and transaction and returns the transaction
     */
    @NotNull
    public Transaction transferFunds(UUID sourceID, UUID destinationID, BigDecimal amount, String description) throws BankingException {
        Account source = accountService.getAccountByID(sourceID);
        Account destination = accountService.getAccountByID(destinationID);

        source.validateTransfer(amount, destination);
        Transaction transaction = new Transaction(TRANSFER, amount, sourceID, destinationID, description);
        try {
            accountService.applyWithdrawal(source, amount);
            accountService.applyDeposit(destination, amount);
            transaction.setTransactionStatus(COMPLETED);
        } catch (Exception e) {
            transaction.setTransactionStatus(FAILED);
            transactionRepository.saveTransaction(transaction);
            throw new TransactionFailedException("Failed to transfer requested funds from " + source.getAccountID() +
                    " account to " + destination.getAccountID() + " account ", e);
        }
        transactionRepository.saveTransaction(transaction);
        return transaction;
    }

    /*
    method to get a transaction via an account ID
     */
    @NotNull
    public Transaction getTransactionByID(UUID transactionID) throws TransactionNotFoundException {
        return transactionRepository.findTransactionById(transactionID)
                .orElseThrow(() -> new TransactionNotFoundException(transactionID));
    }

    /*
    method to get an accounts full transaction history and throws an accoun not found
    exception if the accountID doesn't exist
     */
    @NotNull
    public List<Transaction> getTransactionHistoryForAccount(UUID accountID) throws AccountNotFoundException {
        accountService.getAccountByID(accountID);   // throws if not found
        return transactionRepository.findTransactionByAccountId(accountID);
    }
}
