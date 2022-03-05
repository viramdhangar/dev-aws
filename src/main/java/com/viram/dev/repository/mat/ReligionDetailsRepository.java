package com.viram.dev.repository.mat;

import org.springframework.data.repository.CrudRepository;

import com.viram.dev.dto.mat.BasicDetails;
import com.viram.dev.dto.mat.ReligionDetails;

public interface ReligionDetailsRepository extends CrudRepository<ReligionDetails, Long>{
	public ReligionDetails findByBasicDetail(BasicDetails basicDetail);
}
