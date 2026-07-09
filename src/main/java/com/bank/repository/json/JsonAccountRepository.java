package com.bank.repository.json;

import com.bank.account.Account;
import com.bank.file.FileHelper;
import com.bank.repository.AccountRepository;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class JsonAccountRepository implements AccountRepository {
    private final FileHelper<Account> fileHelper;
    private final Map<UUID, Account> accounts;

    public JsonAccountRepository(FileHelper<Account> fileHelper) {
        this.fileHelper = fileHelper;
        this.accounts = fileHelper.loadFromFile();
    }

    /**
     * @author Derek Homel
     * @summary
     * @param account the account to save
     */
    @Override
    public void saveAccount(Account account) {
        accounts.put(account.getAccountID(), account);
        fileHelper.writeToFile(accounts);
    }

    /**
     * @author Derek Homel
     * @summary
     * @param accountID the account to find
     * @return returns an Optional of the accountID to ensure never null
     */
    @Override
    public @NotNull Optional<Account> findAccountByAccountID(UUID accountID) {
        return Optional.ofNullable(accounts.get(accountID));
    }

    /**
     * @author Derek Homel
     * @param userId the userID associated with the account that is trying to
     * be located
     * @return
     */
    @Override
    public List<Account> findAccountByUserID(UUID userId) {
        return accounts.values().stream()
                .filter(a -> a.getUserID().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Account> findAllAccounts() {
        return new ArrayList<>(accounts.values());
    }

    @Override
    public void deleteAccount(UUID accountId) {
        accounts.remove(accountId);
        fileHelper.writeToFile(accounts);
    }
}
