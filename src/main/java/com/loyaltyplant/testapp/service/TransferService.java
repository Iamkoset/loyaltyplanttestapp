package com.loyaltyplant.testapp.service;

import com.loyaltyplant.testapp.controller.dto.AccountTransferDto;
import com.loyaltyplant.testapp.domain.dao.AccountDao;
import com.loyaltyplant.testapp.domain.model.Account;
import com.loyaltyplant.testapp.exceptions.AccountDoesntExistException;
import com.loyaltyplant.testapp.exceptions.NotEnoughFundsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.locks.ReentrantLock;

@Service("transferService")
@Transactional
public class TransferService {

    @Autowired
    private AccountDao accountDao;

    private ReentrantLock classLock = new ReentrantLock(true);

    public AccountTransferDto transfer(long fromAccount, long toAccount, long amount)
            throws NotEnoughFundsException, AccountDoesntExistException {
        try {
            classLock.lock();
            Account from = accountDao.getOne(fromAccount);
            long currency = from.getCurrency();
            if (currency < amount) {
                throw new NotEnoughFundsException("Account " + fromAccount +
                        " doesn\'t have enough funds to transfer");
            }
            Account to = accountDao.findOne(toAccount);
            if (to == null)
                throw new AccountDoesntExistException("Account " + toAccount + " doesn't exist");

            from.withdrawFunds(amount);
            to.addFunds(amount);
            Account savedFrom = accountDao.saveAndFlush(from);
            Account savedTo = accountDao.saveAndFlush(to);
            AccountTransferDto result = new AccountTransferDto();
            result.setFromAccount(savedFrom.getAccountNumber());
            result.setToAccount(savedTo.getAccountNumber());
            result.setAmount(amount);
            result.setSenderResultCurrency(savedFrom.getCurrency());
            result.setRecipientResultCurrency(savedTo.getCurrency());
            return result;
        } finally {
            classLock.unlock();
        }
    }

    public AccountTransferDto withdraw(long accountNumber, long amount) throws NotEnoughFundsException {
        try {
            classLock.lock();
            Account account = accountDao.getOne(accountNumber);
            long currency = account.getCurrency();
            if (currency < amount) {
                throw new NotEnoughFundsException("Account " + accountNumber +
                        " doesn\'t have enough funds to withdraw");
            }
            account.withdrawFunds(amount);
            account = accountDao.saveAndFlush(account);
            AccountTransferDto result = new AccountTransferDto();
            result.setFromAccount(accountNumber);
            result.setSenderResultCurrency(account.getCurrency());
            return result;
        } finally {
            classLock.unlock();
        }
    }

    public AccountTransferDto add(long accountNumber, long amount) {
        try {
            classLock.lock();
            Account account = accountDao.getOne(accountNumber);
            long currency = account.getCurrency();
            account.addFunds(amount);
            account = accountDao.saveAndFlush(account);
            AccountTransferDto result = new AccountTransferDto();
            result.setFromAccount(accountNumber);
            result.setSenderResultCurrency(account.getCurrency());
            return result;
        } finally {
            classLock.unlock();
        }
    }


}
