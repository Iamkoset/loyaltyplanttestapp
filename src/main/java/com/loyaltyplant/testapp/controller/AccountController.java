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

    /**
     * Returns {@link ResponseEntity} which contain data of created account
     *
     * Response status {@link HttpStatus#NOT_IMPLEMENTED} means that user for
     * account wasn't found in data store
     *
     * @param user user structure
     * @return {@link ResponseEntity}
     */
    @PostMapping
    public ResponseEntity<? super Account> addAccount(@RequestBody User user) {
        Account createdAccount = accountService.addAccount(user.getId());
        if (createdAccount == null)
            return new ResponseEntity<>("Account cannot be created for nonexistent user",
                    HttpStatus.NOT_IMPLEMENTED);
        createdAccount.getUser().setAccountSet(null);
        return new ResponseEntity<>(createdAccount, HttpStatus.OK);
    }


    /**
     * Returns {@link ResponseEntity} which contain data of all accounts
     *
     * Response status {@link HttpStatus#NOT_FOUND} means that accounts
     * wasn't found in data store
     *
     * @return {@link ResponseEntity}
     */
    @GetMapping
    public ResponseEntity<? super List<Account>> getAllAccounts() {
        List<Account> allAccounts = accountService.getAllAccounts();

        if (allAccounts == null)
            return new ResponseEntity<>("Accounts not found", HttpStatus.NOT_FOUND);

        for (Account account : allAccounts)
            account.setUser(null);

        return new ResponseEntity<>(allAccounts, HttpStatus.OK);
    }

    /**
     * Returns {@link ResponseEntity} which contain data of requested account
     *
     * Response status {@link HttpStatus#NOT_FOUND} means that account
     * wasn't found in data store
     * @param id account id
     * @return {@link ResponseEntity}
     */
    @GetMapping("/{id}")
    public ResponseEntity<? super Account> getAccount(@PathVariable Long id) {
        Account account = accountService.getById(id);

        if (account == null)
            return new ResponseEntity<>("No users found with id: " + id, HttpStatus.NOT_FOUND);

        account.setUser(null);
        return new ResponseEntity<>(account, HttpStatus.OK);
    }

    /**
     * Returns {@link ResponseEntity} which contain data of requested accounts
     * of particular user
     *
     * Response status {@link HttpStatus#NOT_FOUND} means that accounts
     * wasn't found in data store
     * @param userId user id
     * @return {@link ResponseEntity}
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<? super List<Account>> getAccountsOfUser(@PathVariable Long userId) {
        List<Account> accounts = accountService.getAccountsOfUser(userId);

        if (accounts == null)
            return new ResponseEntity<>("No users found with id: " + userId, HttpStatus.NOT_FOUND);

        for (Account account : accounts)
            account.setUser(null);
        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }

    /**
     * @param id account id
     * @return {@link ResponseEntity}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        accountService.deleteAccount(id);
//        accountService.deleteAccount(id);
        return new ResponseEntity<>("Account with id=" + id + " has been deleted", HttpStatus.OK);
    }
}
