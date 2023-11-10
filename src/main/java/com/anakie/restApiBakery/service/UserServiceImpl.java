package com.anakie.restApiBakery.service;

import com.anakie.restApiBakery.entity.Address;
import com.anakie.restApiBakery.entity.Role;
import com.anakie.restApiBakery.entity.User;
import com.anakie.restApiBakery.exception.DuplicateEmailException;
import com.anakie.restApiBakery.exception.UserNotFoundException;
import com.anakie.restApiBakery.repository.AddressRepository;
import com.anakie.restApiBakery.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Slf4j
@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AddressRepository addressRepository;

    @Override
    public User save(User user) throws DuplicateEmailException {
        if(userRepository.findAll().stream().anyMatch((eachUser)->eachUser.getEmail().equalsIgnoreCase(user.getEmail()))){
            throw new DuplicateEmailException("Duplicate email: "+user.getEmail()+" , Already exists, log in  or use another email");
        }
        if(user.getRole()==null){
            user.setRole(Role.CUSTOMER_ROLE);
        }
        return userRepository.save(user);

    }

    @Override
    public User findById(Long id) throws UserNotFoundException {
        return userRepository.findById(id).orElseThrow(()->new UserNotFoundException("User "+id+" is not found"));
    }


    @Override
    public void deleteById(Long id)throws UserNotFoundException {
        userRepository.delete(userRepository.findById(id).orElseThrow(()-> new UserNotFoundException("Failed to delete, user "+id+" not found!")));
    }

    @Override
    public User update(User user)throws UserNotFoundException {
        if(!userRepository.existsById(user.getId())){
            throw new UserNotFoundException("Failed to delete, user "+user.getId()+" not found!");
        }
        return userRepository.save(user);
    }
    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional
    @Override
    public User saveAddress(Address address, Long id) throws UserNotFoundException {

        User user=userRepository.findById(id).orElseThrow(
                ()-> new UserNotFoundException("User with id "+id+" is not found")
        );
        user.getAddressList().add(addressRepository.save(address));
            return userRepository.save(user);
    }

    @Override
    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }
}
