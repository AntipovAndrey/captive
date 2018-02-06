package ru.captive.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.captive.dao.AccountDao;
import ru.captive.model.Account;

import java.sql.Date;
import java.time.LocalDate;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountDao accountDao;

    @Autowired
    public AccountServiceImpl(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @Override
    public Iterable<Account> findAll() {
        return accountDao.findAll();
    }

    @Override
    public Account findById(Long id) {
        return accountDao.findOne(id);
    }

    @Override
    public void save(Account account) {
        if (account.getDate() == null) {
            account.setDate(Date.valueOf(LocalDate.now()));
        }
        accountDao.save(account);
    }

    @Override
    public void delete(Account account) {
        accountDao.delete(account);
    }

    @Override
    public Account findByLogin(String login) {
        return accountDao.findOneByLogin(login);
    }
}