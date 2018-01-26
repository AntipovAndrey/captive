package ru.captive.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.captive.model.Account;
import ru.captive.service.AccountService;

@RestController
@RequestMapping("/rest/account")
public class RestAccountController {
    private final AccountService accountService;

    @Autowired
    public RestAccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @RequestMapping("")
    public Iterable<Account> getAll() {
        return accountService.findAll();
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<Account> addAccount(@Validated(Account.New.class) @RequestBody Account account) {
        accountService.save(account);
        return new ResponseEntity<>(account, HttpStatus.CREATED);
    }

}