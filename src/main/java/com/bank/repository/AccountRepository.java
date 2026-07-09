package com.bank.repository;

import com.bank.account.Account;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountRepository {
    void saveAccount(Account account);
    @NotNull Optional<Account> findAccountByAccountID(UUID accountID);
     List<Account> findAccountByUserID(UUID userID);
     List<Account> findAllAccounts();
    void deleteAccount(UUID accountID);
}
