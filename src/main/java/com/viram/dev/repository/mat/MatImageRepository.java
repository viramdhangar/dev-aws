package com.viram.dev.repository.mat;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.viram.dev.dto.mat.BasicDetails;
import com.viram.dev.dto.mat.MatImageModel;

public interface MatImageRepository extends JpaRepository<MatImageModel, Long> {
	Optional<MatImageModel> findByName(String name);
	List<MatImageModel> findByBasicDetail(BasicDetails basicDetail);
	@Modifying
	@Transactional
	//@Query(value = "delete from mat_images where profile_id=?", nativeQuery = true)
	int deleteByBasicDetail(BasicDetails basicDetail);
	
	public List<MatImageModel> findAllByBasicDetail(BasicDetails basicDetail);
}