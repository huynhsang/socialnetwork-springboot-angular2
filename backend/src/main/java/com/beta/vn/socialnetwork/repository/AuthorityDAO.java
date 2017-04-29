package com.beta.vn.socialnetwork.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.beta.vn.socialnetwork.entity.Authority;

@Transactional
public interface AuthorityDAO extends JpaRepository<Authority, Integer>{
}
