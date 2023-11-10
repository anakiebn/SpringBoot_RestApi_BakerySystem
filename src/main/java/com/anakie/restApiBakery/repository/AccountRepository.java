package com.anakie.restApiBakery.repository;

import com.anakie.restApiBakery.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account,Long> {
}
