package com.bank.repository.inMem;

import com.bank.account.Account;
import com.bank.repository.AccountRepository;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class InMemoryAccountRepository implements AccountRepository {
    private final Map<UUID, Account> accounts = new HashMap<>();

    /**
     * @author Derek Homel
     * @summary
     * @param account the account to save
     */
    @Override
    public void saveAccount(Account account) {
        accounts.put(account.getAccountID(), account);
    }

    /**
     * @author Derek Homel
     * @summary
     * @param accountID the accountID to find
     * @return returns the accountID as an Optional
     */
    @Override
    public @NotNull Optional<Account> findAccountByAccountID(UUID accountID) {
        return Optional.ofNullable(accounts.get(accountID));
    }

    /**
     * @author Derek Homel
     * @summary
     * @param userID the userID to find
     * @return returns a list of Collections for the account by userID
     */
    @Override
    public List<Account> findAccountByUserID(UUID userID) {
        return accounts.values().stream()
                .filter(account -> account.getUserID().equals(userID))
                .collect(Collectors.toList());
    }

    /**
     * @author Derek Homel
     * @summary
     * @return returns a List of all accounts for current user
     */
    @Override
    public List<Account> findAllAccounts() {
        return new ArrayList<>(accounts.values());
    }

    /**
     * @author Derek Homel
     * @summary
     * @param accountID the accountID to remove
     */
    @Override
    public void deleteAccount(UUID accountID) {
        accounts.remove(accountID);
    }
}
