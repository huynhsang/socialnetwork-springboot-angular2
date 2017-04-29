package com.beta.vn.socialnetwork.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.beta.vn.socialnetwork.entity.User;

@Transactional
public interface UserDAO extends JpaRepository<User, Integer>{

	User findByEmail(String username);
	
	/*@Query( " select u from User u where u.id in :ids " )
	List<User> getByUserIds(@Param("ids") List<Integer> ids);*/
	
	
	
	
}
