package com.viram.dev.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.viram.dev.dto.Membership;

public interface MembershipRepository extends CrudRepository<Membership, Integer>{
	List<Membership> findAllByUserId(Long userId);
	@Query(value = "select * from membership order by created desc limit ?,?", nativeQuery = true)
	public List<Membership> findAllMembershipByLimit(int offset,  int limit);
}
