package com.viram.dev.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.viram.dev.dto.Address;
import com.viram.dev.repository.AddressRepository;
import com.viram.dev.repository.UserRepository;

@CrossOrigin (origins = {"*"}, maxAge = 3600)
@RestController
public class AddressController {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AddressRepository addressRepository;

	@GetMapping("/addressesByUser/{userId}")
	public Address addresses(@PathVariable Long userId) {
		return addressRepository.findByUserId(userId);
	}
	
	@GetMapping("/addressById/{id}")
	public Optional<Address> address(@PathVariable Long id) {
		return addressRepository.findById(id);
	}
	
	@PostMapping("/address/{userId}")
	public Optional<Object> saveAddress(@PathVariable (value = "userId") Long userId, @Validated @RequestBody Address address) {
		return userRepository.findById(userId).map(user -> {
			address.setUser(user);
            addressRepository.save(address);
            return addresses(userId);
        });
	}
}
