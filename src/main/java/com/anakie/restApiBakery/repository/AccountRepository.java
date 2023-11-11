package com.anakie.restApiBakery.repository;

import com.anakie.restApiBakery.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account,Long> {


    @Query(value = "SELECT * FROM `account`  WHERE user_id = ?1",nativeQuery = true)
    Optional<Account> findByUserId(Long userId);
}
