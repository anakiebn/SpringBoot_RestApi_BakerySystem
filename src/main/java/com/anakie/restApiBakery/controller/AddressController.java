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

    @GetMapping("/{id}")
    public ResponseEntity<Address> getAddress(@PathVariable Long id) {
        try {
            log.info("Getting address by ID: {}", id);
            return new ResponseEntity<>(addressService.findById(id), HttpStatus.OK);
        } catch (AddressNotFoundException ex) {
            log.error("Exception occurred: " + ex.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<Address> addAddress(@RequestBody Address address) {
        log.info("Adding address");
        Address addedAddress = addressService.save(address);
        return new ResponseEntity<>(addedAddress, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Address> updateAddress(@RequestBody Address address) {
        try {
            return new ResponseEntity<>(addressService.update(address), HttpStatus.ACCEPTED);
        } catch (AddressNotFoundException ex) {
            log.error("Exception occurred: " + ex.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        try {
            addressService.deleteById(id);
            return new ResponseEntity<>("Address id: "+id+", successfully deleted ", HttpStatus.OK);
        } catch (AddressNotFoundException ex) {
            log.error("Exception occurred: " + ex.getMessage());
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
