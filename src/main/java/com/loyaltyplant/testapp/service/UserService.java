package com.loyaltyplant.testapp.service;

import com.loyaltyplant.testapp.domain.dao.AccountDao;
import com.loyaltyplant.testapp.domain.dao.UserDao;
import com.loyaltyplant.testapp.domain.model.Account;
import com.loyaltyplant.testapp.domain.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("userService")
@Transactional
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private AccountDao accountDao;

    public User addUser(User user) {
        return userDao.saveAndFlush(user);
    }

    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    public User getById(Long id) {
        return userDao.getOne(id);
    }

    public User updateUser(User user) {
        return userDao.saveAndFlush(user);
    }

    public void deleteUser(Long id) {
        List<Account> accountsOfUser = accountDao.getAccountsOfUser(id);
        for (Account account : accountsOfUser) {
            accountDao.delete(account);
        }
        userDao.delete(id);
    }
}
