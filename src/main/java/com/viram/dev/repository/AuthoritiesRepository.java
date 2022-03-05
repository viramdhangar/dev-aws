package com.viram.dev.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.viram.dev.dto.Authorities;

public interface AuthoritiesRepository extends CrudRepository<Authorities, Long> {

	public List<Authorities> findAllById(Long id);
	public List<Authorities> findByUserId(Long userId);

}
