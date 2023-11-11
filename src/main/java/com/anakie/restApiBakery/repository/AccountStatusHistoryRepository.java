package com.anakie.restApiBakery.repository;

import com.anakie.restApiBakery.entity.AccountStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountStatusHistoryRepository extends JpaRepository<AccountStatusHistory,Long> {
}
