package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;

@Service
public class AccountService {
    // Account repository that this class will use
    @Autowired
    private AccountRepository accountRepository;

    // @Autowired
    // public AccountService(AccountRepository accountRepository) {
    //     this.accountRepository = accountRepository;
    // }

    /**
     * TODO: Handle input validation
     * This method will tell the account repo to save a new account
     * @param account the Account we wish to register
     */
    public void register(Account account) {
        accountRepository.save(account);
    }
}
