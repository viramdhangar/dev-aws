package com.viram.dev.repository.mat;

import org.springframework.data.repository.CrudRepository;

import com.viram.dev.dto.mat.BasicDetails;
import com.viram.dev.dto.mat.ProfessionalDetails;

public interface ProfessionalDetailsRepository extends CrudRepository<ProfessionalDetails, Long>{
	public ProfessionalDetails findByBasicDetail(BasicDetails basicDetail);
}
