package com.viram.dev.repository.mat;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.viram.dev.dto.mat.BasicDetails;
import com.viram.dev.dto.mat.PersonalDetails;

@Repository
public interface PersonalDetailsRepository  extends CrudRepository<PersonalDetails, Long> {

	PersonalDetails findByBasicDetail(BasicDetails basicDetail);
	
}
