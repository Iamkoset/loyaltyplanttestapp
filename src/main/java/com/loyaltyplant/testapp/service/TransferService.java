package com.loyaltyplant.testapp.service;

import com.loyaltyplant.testapp.controller.dto.AccountTransferDto;
import com.loyaltyplant.testapp.domain.dao.AccountDao;
import com.loyaltyplant.testapp.domain.model.Account;
import com.loyaltyplant.testapp.exceptions.NonexistentAccountException;
import com.loyaltyplant.testapp.exceptions.NotEnoughFundsException;
import com.loyaltyplant.testapp.service.sync.Locker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("transferService")
public class TransferService {

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private LockerService lockerService;

    @Transactional
    public AccountTransferDto transfer(long fromAccount, long toAccount, long amount)
            throws NotEnoughFundsException, NonexistentAccountException {

        Account from = accountDao.getOne(fromAccount);
        if (from == null)
            throw new NonexistentAccountException("Account " + toAccount + " doesn't exist");

        long currency = from.getCurrency();
        if (currency < amount) {
            throw new NotEnoughFundsException("Account " + fromAccount +
                    " doesn\'t have enough funds to transfer");
        }
        Account to = accountDao.findOne(toAccount);

        if (to == null)
            throw new NonexistentAccountException("Account " + toAccount + " doesn't exist");

        AccountTransferDto result = new AccountTransferDto();
        Long lowestAccountNumber = (from.getAccountNumber() < to.getAccountNumber()) ?
                from.getAccountNumber() : to.getAccountNumber();

        Locker lowestLocker = lockerService.getLockFor(lowestAccountNumber);
        synchronized (lowestLocker) {
            Long highestAccountNumber = (from.getAccountNumber() > to.getAccountNumber()) ?
                    from.getAccountNumber() : to.getAccountNumber();

            Locker highestLocker = lockerService.getLockFor(highestAccountNumber);
            synchronized (highestLocker) {
                from.withdrawFunds(amount);
                to.addFunds(amount);
                Account savedFrom = accountDao.saveAndFlush(from);
                Account savedTo = accountDao.saveAndFlush(to);

                result.setFromAccount(savedFrom.getAccountNumber());
                result.setToAccount(savedTo.getAccountNumber());
                result.setAmount(amount);
                result.setSenderResultCurrency(savedFrom.getCurrency());
                result.setRecipientResultCurrency(savedTo.getCurrency());
            }
        }

        return result;
    }

    @Transactional
    public AccountTransferDto withdraw(long accountNumber, long amount) throws NotEnoughFundsException {

        Account account = accountDao.getOne(accountNumber);
        long currency = account.getCurrency();
        if (currency < amount) {
            throw new NotEnoughFundsException("Account " + accountNumber +
                    " doesn\'t have enough funds to withdraw");
        }

        AccountTransferDto result = new AccountTransferDto();
        Locker locker = lockerService.getLockFor(accountNumber);

        synchronized (locker) {
            account.withdrawFunds(amount);
            account = accountDao.saveAndFlush(account);
            result.setFromAccount(accountNumber);
            result.setSenderResultCurrency(account.getCurrency());
        }
        return result;
    }

    @Transactional
    public AccountTransferDto add(long accountNumber, long amount) {

        Account account = accountDao.getOne(accountNumber);
        AccountTransferDto result = new AccountTransferDto();
        Locker locker = lockerService.getLockFor(accountNumber);
        synchronized (locker) {
            account.addFunds(amount);
            account = accountDao.saveAndFlush(account);
            result.setFromAccount(accountNumber);
            result.setSenderResultCurrency(account.getCurrency());
        }
        return result;
    }
}
