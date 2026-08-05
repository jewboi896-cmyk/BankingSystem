package com.bank.config;

import com.bank.account.Account;
import com.bank.file.FileHelper;
import com.bank.repository.AccountRepository;
import com.bank.repository.TransactionRepository;
import com.bank.repository.UserRepository;
import com.bank.repository.json.JsonAccountRepository;
import com.bank.repository.json.JsonTransactionRepository;
import com.bank.repository.json.JsonUserRepository;
import com.bank.transaction.Transaction;
import com.bank.user.User;
import com.google.gson.reflect.TypeToken;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.UUID;

@Configuration
public class RepoConfig {

    @Bean
    public UserRepository saveToUserRepository() {
        FileHelper<User> helper = new FileHelper<>("data/users.json", new TypeToken<Map<UUID, User>>(){}
                .getType());
        return new JsonUserRepository(helper);
    }

    @Bean
    public AccountRepository saveToAccountRepository() {
        FileHelper<Account> helper = new FileHelper<>("data/accounts.json", new TypeToken<
                Map<UUID, Account>>(){}.getType());
        return new JsonAccountRepository(helper);
    }

    @Bean
    public TransactionRepository saveToTransactionRepository() {
        FileHelper<Transaction> helper = new FileHelper<>("data/transactions.json", new TypeToken<Map
                <UUID, Transaction>>(){}.getType());
        return new JsonTransactionRepository(helper);
    }
}
