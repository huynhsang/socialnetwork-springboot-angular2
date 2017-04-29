package com.beta.vn.socialnetwork.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.beta.vn.socialnetwork.entity.Relationship;

@Transactional
public interface RelationshipDAO extends JpaRepository<Relationship, Integer>{
	
	@Query("select r from Relationship r where r.user.id = :user_one_id AND r.friendId = :user_two_id")
	Relationship getByFiendId(@Param("user_one_id") int id1, @Param("user_two_id") int id2);
	
	@Query("select r from Relationship r where r.user.id = :user_id")
	List<Relationship> getByUserId(@Param("user_id") int id);
}
