package com.loyaltyplant.testapp.domain.dao;

import com.loyaltyplant.testapp.domain.model.Account;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AccountDao extends JpaRepository<Account, Long> {

    @Query(value = "select * from account  where user_id = ?1", nativeQuery = true)
    List<Account> getAccountsOfUser(long userId);
}
