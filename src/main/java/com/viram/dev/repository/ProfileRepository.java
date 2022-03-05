package com.viram.dev.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.viram.dev.dto.Profile;

@Repository
public interface ProfileRepository extends CrudRepository<Profile, Integer>{

}
