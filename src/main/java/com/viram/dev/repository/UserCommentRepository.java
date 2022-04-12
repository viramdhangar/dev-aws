package com.viram.dev.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.viram.dev.dto.Post;
import com.viram.dev.dto.UserComment;

public interface UserCommentRepository extends JpaRepository<UserComment, Long>{

	long countByPost(Post post);
	List<UserComment> findAllByPost(Post post);
}

