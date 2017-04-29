package com.beta.vn.socialnetwork.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.beta.vn.socialnetwork.entity.Post;


@Transactional
public interface PostDAO extends JpaRepository<Post, Integer>{
	
	@Query("select p from Post p where p.user.id in :ids")
	List<Post> getByUseIds(@Param("ids") List<Integer> ids);
}