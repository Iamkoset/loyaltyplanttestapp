package com.loyaltyplant.testapp.controller;

import com.loyaltyplant.testapp.domain.model.Account;
import com.loyaltyplant.testapp.domain.model.User;
import com.loyaltyplant.testapp.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping(value = "/accounts")
public class AccountController {

    @Resource(name = "accountService")
    private AccountService accountService;

    public AccountController() {
    }

    @PostMapping
    public ResponseEntity<Account> addAccount(@RequestBody User user) {
        Account createdAccount = accountService.addAccount(user.getId());
        createdAccount.getUser().setAccountSet(null);
        return new ResponseEntity<>(createdAccount, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<? super List<Account>> getAllAccounts() {
        List<Account> allAccounts = accountService.getAllAccounts();

        if (allAccounts == null)
            return new ResponseEntity<>("Accounts not found", HttpStatus.NOT_FOUND);

        for (Account account : allAccounts)
            account.setUser(null);

        return new ResponseEntity<>(allAccounts, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<? super Account> getAccount(@PathVariable Long id) {
        Account account = accountService.getById(id);

        if (account == null)
            return new ResponseEntity<>("No users found with id: " + id, HttpStatus.NOT_FOUND);

        account.setUser(null);
        return new ResponseEntity<>(account, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<? super List<Account>> getAccountsOfUser(@PathVariable Long userId) {
        List<Account> accounts = accountService.getAccountsOfUser(userId);

        if (accounts == null)
            return new ResponseEntity<>("No users found with id: " + userId, HttpStatus.NOT_FOUND);

        for (Account account : accounts)
            account.setUser(null);
        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }


//    @PutMapping("/{id}")
//    public ResponseEntity updateAccount(@RequestBody Account account) {
////        Account updatedAccount = accountService.updateAccount(account);
////
////        if (updatedAccount == null)
////            return new ResponseEntity("No account with id=" + account.getAccountNumber() + " found", HttpStatus.NOT_FOUND);
//
//        return new ResponseEntity(account, HttpStatus.OK);
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        accountService.deleteAccount(id);
//        accountService.deleteAccount(id);
        return new ResponseEntity<>("Account with id=" + id + " has been deleted", HttpStatus.OK);
    }
}
