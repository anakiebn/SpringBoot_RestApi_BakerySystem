package com.anakie.restApiBakery.repository;

import com.anakie.restApiBakery.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address,Long> {

}

