package com.viram.dev.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.viram.dev.dto.SecretDTO;

@Repository
public interface SecretRepo extends JpaRepository<SecretDTO, Integer>{
	public SecretDTO findByType(String type);
}
