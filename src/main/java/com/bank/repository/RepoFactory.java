package com.bank.repository;

import com.bank.account.Account;
import com.bank.file.FileHelper;
import com.bank.repository.json.JsonAccountRepository;
import com.bank.repository.json.JsonTransactionRepository;
import com.bank.repository.json.JsonUserRepository;
import com.bank.transaction.Transaction;
import com.bank.user.User;
import com.google.gson.reflect.TypeToken;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;

public class RepoFactory {
    public static @NotNull UserRepository createUserRepo() {
        FileHelper<User> userHelper = new FileHelper<>("data/users.json",
                new TypeToken<Map<UUID, User>>(){}.getType());
        return new JsonUserRepository(userHelper);
    }

    public static @NotNull AccountRepository createAccountRepo() {
        FileHelper<Account> accountHelper = new FileHelper<>("data/accounts.json",
                new TypeToken<Map<UUID, Account>>(){}.getType());
        return new JsonAccountRepository(accountHelper);
    }

    public static @NotNull TransactionRepository createTransactionRepo() {
        FileHelper<Transaction> transactionHelper = new FileHelper<>("data/transactions.json",
                new TypeToken<Map<UUID, Transaction>>(){}.getType());
        return new JsonTransactionRepository(transactionHelper);
    }
}
