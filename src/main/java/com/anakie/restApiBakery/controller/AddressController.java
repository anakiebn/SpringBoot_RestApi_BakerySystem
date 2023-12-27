package com.anakie.restApiBakery.controller;

import com.anakie.restApiBakery.entity.Address;
import com.anakie.restApiBakery.exception.AddressNotFoundException;
import com.anakie.restApiBakery.service.AddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/addresses")
public class AddressController {
    @Autowired
    private AddressService addressService;

    @GetMapping("/id")
    public ResponseEntity<Address> findById(@PathVariable Long id) throws AddressNotFoundException {

            log.info("Getting address by ID: {}", id);
            return new ResponseEntity<>(addressService.findById(id), HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<Address> save(@RequestBody Address address) {
        log.info("Adding address");
        Address addedAddress = addressService.save(address);
        return new ResponseEntity<>(addedAddress, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<Address> update(@RequestBody Address address) throws AddressNotFoundException {

            return new ResponseEntity<>(addressService.update(address), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/id")
    public ResponseEntity<String> deleteById(@PathVariable Long id) throws AddressNotFoundException {
            addressService.deleteById(id);
            return new ResponseEntity<>("Address id: "+id+", successfully deleted ", HttpStatus.OK);
    }
}
