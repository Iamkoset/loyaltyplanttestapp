package com.loyaltyplant.testapp.service;

import com.loyaltyplant.testapp.controller.dto.AccountTransferDto;
import com.loyaltyplant.testapp.domain.dao.AccountDao;
import com.loyaltyplant.testapp.domain.model.Account;
import com.loyaltyplant.testapp.exceptions.AccountDoesntExistException;
import com.loyaltyplant.testapp.exceptions.NotEnoughFundsException;
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
            throws NotEnoughFundsException, AccountDoesntExistException {

        Account from = accountDao.getOne(fromAccount);
        long currency = from.getCurrency();
        if (currency < amount) {
            throw new NotEnoughFundsException("Account " + fromAccount +
                    " doesn\'t have enough funds to transfer");
        }
        Account to = accountDao.findOne(toAccount);

        if (to == null)
            throw new AccountDoesntExistException("Account " + toAccount + " doesn't exist");

        AccountTransferDto result = new AccountTransferDto();
        Long lowestNumber = (from.getAccountNumber() < to.getAccountNumber()) ?
                from.getAccountNumber() : to.getAccountNumber();

        Object lowestLocker = lockerService.getRegistry().get(lowestNumber);
        if (lowestLocker == null)
            lockerService.getRegistry().put(lowestNumber, new Object());

        lowestLocker = lockerService.getRegistry().putIfAbsent(lowestNumber, new Object());
        synchronized (lowestLocker) {
            Long highestNumber = (from.getAccountNumber() > to.getAccountNumber()) ?
                    from.getAccountNumber() : to.getAccountNumber();

            Object highestLocker = lockerService.getRegistry().get(highestNumber);
            if (highestLocker == null)
                lockerService.getRegistry().put(highestNumber, new Object());

            highestLocker = lockerService.getRegistry().putIfAbsent(highestNumber, new Object());

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
        Object locker = lockerService.getRegistry().get(accountNumber);
        if (locker == null)
            lockerService.getRegistry().put(accountNumber, new Object());

        locker = lockerService.getRegistry().putIfAbsent(accountNumber, new Object());

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
        Object locker = lockerService.getRegistry().get(accountNumber);
        if (locker == null)
            lockerService.getRegistry().put(accountNumber, new Object());

        locker = lockerService.getRegistry().putIfAbsent(accountNumber, new Object());
        synchronized (locker) {
            account.addFunds(amount);
            account = accountDao.saveAndFlush(account);
            result.setFromAccount(accountNumber);
            result.setSenderResultCurrency(account.getCurrency());
        }
        return result;
    }
}
