package com.coder.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.coder.Repository.UserRepo;
import com.coder.dto.User;


@Component
public class CustomUserDetailService implements UserDetailsService{

	@Autowired
	private UserRepo repo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user=repo.findByEmail(username);
		if(user==null) {
			throw new UsernameNotFoundException("user not found");
		}
		else {
			return new CustomUser(user);
		}
	}

}
