package com.anakie.restApiBakery.repository;

import com.anakie.restApiBakery.entity.Address;
import com.anakie.restApiBakery.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    boolean existsByEmail(String email);

}