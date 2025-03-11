package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.exception.AccountRegistrationException;
import com.example.exception.DuplicateUsernameException;
import com.example.repository.AccountRepository;

@Service
public class AccountService {
    // Account repository that this class will use
    @Autowired
    private AccountRepository accountRepository;

    /**
     * This method will tell the account repo to save a new account
     * @param account the Account we wish to register
     */
    public void register(Account account) {
        validateAccount(account);
        accountRepository.save(account);
    }

    private void validateAccount(Account account) {
        if (!usernameAvailable(account.getUsername())) {
            throw new DuplicateUsernameException(
                    "An account with this username already exists. " +
                            "Please choose a different username."
            );
        }

        if (!isValidUsernameAndPassword(account.getUsername(), account.getPassword())) {
            throw new AccountRegistrationException(
                    "Your username must not be blank and your password " +
                            "must be at least 4 characters."
            );
        }
    }

    private boolean usernameAvailable(String username) {
        return accountRepository.findByUsername(username).isEmpty();
    }

    private boolean isValidUsernameAndPassword(String username, String password) {
        return isValidUsername(username) && isValidPassword(password);
    }

    private boolean isValidUsername(String username) {
        return !username.isEmpty();
    }

    private boolean isValidPassword(String password) {
        return password.length() > 4;
    }
}
