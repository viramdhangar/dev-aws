package com.viram.dev.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.viram.dev.dto.DAOUser;
import com.viram.dev.dto.Post;
import com.viram.dev.dto.UserLike;

public interface UserLikeRepository extends JpaRepository<UserLike, Long>{

	long countByPost(Post post);
	List<UserLike> findAllByPost(Post post);
	void deleteByPostAndUser(Post post, DAOUser user);
}
