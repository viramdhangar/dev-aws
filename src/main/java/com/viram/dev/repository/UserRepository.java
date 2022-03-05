package com.viram.dev.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.viram.dev.dto.DAOUser;


public interface UserRepository  extends CrudRepository<DAOUser, Long> {

	DAOUser findByUsername(String username);
	DAOUser findByEmail(String email);
	@Query(value="update user set first_name=?, phone=?, pin_code=? where id=?", nativeQuery = true)
	DAOUser update(DAOUser user);
	//@Query(value="update user set password=? where id=?", nativeQuery = true)
	//DAOUser updateP(DAOUser user);
	
}
