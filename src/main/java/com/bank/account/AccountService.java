package com.bank.account;

import com.bank.exception.*;
import com.bank.repository.AccountRepository;
import com.bank.repository.UserRepository;
import org.jetbrains.annotations.NotNull;
import com.bank.exception.AccountFrozenException.FrozenOp;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final ConcurrentHashMap<UUID, Object> accountLocks = new ConcurrentHashMap<>();
    private static final BigDecimal DEFAULT_OVERDRAFT_LIMIT = BigDecimal.ZERO;
    private static final BigDecimal DEFAULT_SAVINGS_INTEREST_RATE =
            new BigDecimal("0.01");
    private static final int DEFAULT_MONTHLY_WITHDRAWAL_LIMIT = 6;

    public AccountService(AccountRepository accountRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    // this method exists to ensure transaction is synced the whole time
    /**
     * @author Derek Homel
     * @param accountId account id to validate
     * @return returns a new object if account isnt locked
     */
    public Object lockFor(UUID accountId) {
        return accountLocks.computeIfAbsent(accountId, id -> new Object());
    }

    // create checking account

    /**
     * @author Derek Homel
     * @param userID id of the logged on user
     * @return returns the requested checking account
     * @throws UserNotFoundException throws if user isnt found
     */
    public Account createCheckingAccount(UUID userID) throws UserNotFoundException {
        requireUserExists(userID);
        CheckingAccount account = new CheckingAccount(userID, DEFAULT_OVERDRAFT_LIMIT);
        accountRepository.saveAccount(account);
        return account;
    }
    // create savings account

    /**
     * @author Derek Homel
     * @param userID id of the user requesting the savings account creation
     * @return returns the new savings account
     * @throws UserNotFoundException throws if user isnt found
     */
    public Account createSavingsAccount(UUID userID) throws UserNotFoundException {
        requireUserExists(userID);
        SavingsAccount account = new SavingsAccount(userID, DEFAULT_SAVINGS_INTEREST_RATE,
                DEFAULT_MONTHLY_WITHDRAWAL_LIMIT);
        accountRepository.saveAccount(account);
        return account;
    }
    // lookup user by accountID

    /**
     * @author Derek Homel
     * @param accountID logged-in users account id
     * @return returns the account id of the specified account
     * @throws AccountNotFoundException throws if specified account isnt found
     */
    public Account getAccountByID(UUID accountID) throws AccountNotFoundException {
        return accountRepository.findAccountByAccountID(accountID)
                .orElseThrow(() -> new AccountNotFoundException(accountID));
    }

    /** get all accounts of a given user
     * @author Derek Homel
     @param userID the logged-in users unique id
     @return List<Account> returns a list of all accounts of all types
     that belong to that userID
     */
    public @NotNull List<Account> getAllUserAccounts(UUID userID) {
        return accountRepository.findAccountByUserID(userID);
    }

    // freezes an account
    /**
     * @author Derek Homel
     * @param accountID logged-in users accountID
     * @throws AccountNotFoundException throws if specified account isnt found
     * @throws AccountClosedException throws if specified account is closed
     */
    public void freezeAccount(UUID accountID) throws AccountNotFoundException, AccountClosedException {
        synchronized (lockFor(accountID)) {
            Account account = accountRepository.findAccountByAccountID(accountID).orElseThrow(() ->
                    new AccountNotFoundException(accountID));

            if (account.getAccountStatus().equals(AccountStatus.CLOSED)) {
                throw new AccountClosedException(accountID);
            }
            account.setAccountStatus(AccountStatus.FROZEN);
            accountRepository.saveAccount(account);
        }
    }
    // closes an account

    /**
     * @author Derek Homel
     * @param accountID account id of the requested account tot be closed
     * @throws AccountNotFoundException throws if account isnt found
     * @throws CannotCloseAccountException throws if account cannot be closed
     * @throws AccountFrozenException throws if account is frozen.
     * account must be frozen before closing
     */
    public void closeAccount(UUID accountID) throws AccountNotFoundException, CannotCloseAccountException,
            AccountFrozenException {
        synchronized (lockFor(accountID)) {
            Account account = accountRepository.findAccountByAccountID(accountID).orElseThrow(() ->
                    new AccountNotFoundException(accountID));

            if (account.getAccountStatus().equals(AccountStatus.FROZEN)) {
                throw new AccountFrozenException(accountID, FrozenOp.CLOSURE);
            }

            BigDecimal currentBalance = account.getAccountBalance();
            // don't allow an account to close if account balance isnt 0
            if (currentBalance.compareTo(BigDecimal.ZERO) != 0) {
                throw new CannotCloseAccountException(accountID);
            }

            if (!account.getAccountStatus().equals(AccountStatus.CLOSED)) {
                account.setAccountStatus(AccountStatus.CLOSED);
                accountRepository.saveAccount(account);
            }
        }
    }

    /**
     * @author Derek Homel
     * @param userID id of the user
     * @throws UserNotFoundException throws if user isnt found
     */
    private void requireUserExists(UUID userID) throws UserNotFoundException {
        if (userRepository.findUserById(userID).isEmpty()) {
            throw new UserNotFoundException("User not found");
        }
    }

    // === These next two methods are public so that balance can be adjusted
    // outside of this package ===

    /**
     * @author Derek Homel
     * @param account account to apply withdrawal from
     * @param amount amount to be withdrawn
     */
    public void applyWithdrawal(@NotNull Account account, BigDecimal amount) {
        account.withdrawFunds(amount);
        accountRepository.saveAccount(account);
    }

    /**
     * @author Derek Homel
     * @param account account to apply the deposit to
     * @param amount amount to be deposited
     */
    public void applyDeposit(@NotNull Account account, BigDecimal amount) {
        account.depositFunds(amount);
        accountRepository.saveAccount(account);
    }
}
