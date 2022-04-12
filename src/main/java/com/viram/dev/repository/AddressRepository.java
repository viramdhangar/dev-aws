package com.viram.dev.repository;

import org.springframework.data.repository.CrudRepository;

import com.viram.dev.dto.Address;

public interface AddressRepository extends CrudRepository<Address, Long>{
	Address findByUserId(Long userId);
}
