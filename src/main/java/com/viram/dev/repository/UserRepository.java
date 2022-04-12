package com.viram.dev.repository;

import org.springframework.data.repository.CrudRepository;

import com.viram.dev.dto.DAOUser;


public interface UserRepository  extends CrudRepository<DAOUser, Long> {

	DAOUser findByUsername(String username);
	DAOUser findByEmail(String email);
}
