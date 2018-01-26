package ru.captive.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.captive.model.Account;

public interface AccountDao extends JpaRepository<Account, Long>{
}
