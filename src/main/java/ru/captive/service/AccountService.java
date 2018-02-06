package ru.captive.service;

import ru.captive.model.Account;

public interface AccountService {
    Iterable<Account> findAll();

    Account findById(Long id);

    void save(Account account);

    void delete(Account account);

    Account findByLogin(String login);
}
