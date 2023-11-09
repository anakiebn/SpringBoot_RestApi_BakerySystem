package com.anakie.restApiBakery.service;

import com.anakie.restApiBakery.entity.Address;
import com.anakie.restApiBakery.exception.AddressNotFoundException;

import java.util.List;

public interface AddressService {

     Address save(Address address);
     Address findById(Long id) throws AddressNotFoundException;
     List<Address> findAll();
     void deleteById(Long id) throws AddressNotFoundException;

     Address update(Address address) throws AddressNotFoundException;



}
