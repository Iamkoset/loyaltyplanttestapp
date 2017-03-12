package com.loyaltyplant.testapp.domain.dao;

import com.loyaltyplant.testapp.domain.model.User;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<User, Long> {

}
