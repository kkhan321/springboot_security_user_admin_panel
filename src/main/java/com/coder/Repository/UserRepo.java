package com.coder.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.coder.dto.User;

public interface UserRepo extends JpaRepository<User, Integer>{

	public User findByEmail(String email);
	
}
