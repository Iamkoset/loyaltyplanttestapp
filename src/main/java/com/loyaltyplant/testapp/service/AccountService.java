package com.loyaltyplant.testapp.service;

import com.loyaltyplant.testapp.domain.dao.AccountDao;
import com.loyaltyplant.testapp.domain.dao.UserDao;
import com.loyaltyplant.testapp.domain.model.Account;
import com.loyaltyplant.testapp.domain.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("accountService")
public class AccountService {

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private UserDao userDao;

    @Transactional
    public Account addAccount(Long userId) {
        User user = userDao.findOne(userId);
        if (user == null)
            return null;

        Account account = new Account();
        account.setUser(user);
        return accountDao.saveAndFlush(account);
    }

    public List<Account> getAllAccounts() {
        return accountDao.findAll();
    }

    public List<Account> getAccountsOfUser(Long userId) {
        return accountDao.getAccountsOfUser(userId);
    }

    public Account getById(Long id) {
        return accountDao.getOne(id);
    }

    public Account updateAccount(Account account) {
        return accountDao.saveAndFlush(account);
    }

    @Transactional
    public void deleteAccount(Long id) {
        accountDao.delete(id);
    }
}
