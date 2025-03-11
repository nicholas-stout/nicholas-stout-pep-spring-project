package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.exception.AccountRegistrationException;
import com.example.exception.AuthenticationException;
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
     * @throws DuplicateUsernameException thrown if the username already exists
     * @throws AccountRegistrationException thrown if the username or password are invalid
     */
    public void register(Account account) throws DuplicateUsernameException, AccountRegistrationException {
        validateAccount(account);
        accountRepository.save(account);
    }

    /**
     * This method will query the account repo for an existing user who is trying to login
     * @param account the Account we are trying to log in to
     * @return the logged-in Account, if it exists
     * @throws AuthenticationException thrown if the username or password are incorrect
     */
    public Account login(Account account) throws AuthenticationException {
        return accountRepository.findByUsernameAndPassword(account.getUsername(), account.getPassword())
                .orElseThrow(() -> new AuthenticationException("Your username or password are incorrect. Please try again."));
    }

    /**
     * This method will verify if a new Account can be created with the data contained in an Account object
     * @param account the Account to be validated
     * @throws DuplicateUsernameException thrown if the username already exists
     * @throws AccountRegistrationException thrown if the username or password are invalid
     */
    private void validateAccount(Account account) throws DuplicateUsernameException, AccountRegistrationException {
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

    /**
     * This method validates whether a username is available
     * @param username the username in question
     * @return true or false depending on whether the username is available
     */
    private boolean usernameAvailable(String username) {
        return accountRepository.findByUsername(username).isEmpty();
    }

    /**
     * This method validates a username and password
     * @param username the username we wish to validate. A username is valid if it is not blank.
     * @param password the password we wish to validate. A password is valid if it contains at least 4 characters.
     * @return true or false depending on whether the username and password are valid.
     */
    private boolean isValidUsernameAndPassword(String username, String password) {
        return isValidUsername(username) && isValidPassword(password);
    }

    /**
     * This method validates a username
     * @param username the username we wish to validate. A username is valid if it is not blank
     * @return true or false depending on whether the username is valid
     */
    private boolean isValidUsername(String username) {
        return !username.isEmpty();
    }

    /**
     * This method validates a password
     * @param password the password we wish to validate. A password is valid if it contains at least 4 characters.
     * @return true or false depending on whether the password is valid
     */
    private boolean isValidPassword(String password) {
        return password.length() > 4;
    }
}
