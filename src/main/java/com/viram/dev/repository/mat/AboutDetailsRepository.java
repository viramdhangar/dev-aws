package com.viram.dev.repository.mat;

import org.springframework.data.repository.CrudRepository;

import com.viram.dev.dto.mat.AboutDetails;
import com.viram.dev.dto.mat.BasicDetails;

public interface AboutDetailsRepository extends CrudRepository<AboutDetails, Long>{
	public AboutDetails findByBasicDetail(BasicDetails basicDetail);
}
