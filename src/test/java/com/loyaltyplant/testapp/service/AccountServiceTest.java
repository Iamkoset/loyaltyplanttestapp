package com.loyaltyplant.testapp.service;

import com.loyaltyplant.testapp.domain.dao.AccountDao;
import com.loyaltyplant.testapp.domain.dao.UserDao;
import com.loyaltyplant.testapp.domain.model.Account;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class AccountServiceTest {


    private AccountDao accountDaoMock = mock(AccountDao.class);
    private UserDao userDaoMock = mock(UserDao.class);

    @InjectMocks
    private AccountService service = new AccountService();

    @Test
    public void addAccount_UserNull() throws Exception {
        when(userDaoMock.getOne(any())).thenReturn(null);
        when(accountDaoMock.saveAndFlush(any(Account.class))).thenReturn(new Account());

        Account account = service.addAccount(1234L);
        assertNull(account);
    }

}