package com.viram.dev.repository.mat;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.viram.dev.dto.mat.BasicDetails;

@Repository
public interface BasicDetailsRepository extends CrudRepository<BasicDetails, Long> {
	public List<BasicDetails> findAllByUserId(Long userId);
	@Query(value = "select * from basic_details order by created desc limit ?,?", nativeQuery = true)
	public List<BasicDetails> findAllProfileByLimit(int offset,  int limit);
}