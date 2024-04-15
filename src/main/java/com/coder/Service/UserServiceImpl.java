package com.coder.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.coder.Repository.UserRepo;
import com.coder.dto.User;

import jakarta.servlet.http.HttpSession;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepo userRepo;
	
	
	@Override
	public User saveUser(User user) {
		return userRepo.save(user);
		
	}

	public void removeSessionMessage() {
		HttpSession session= ((ServletRequestAttributes)(RequestContextHolder.currentRequestAttributes())).getRequest()
        .getSession();
		session.removeAttribute("msg");

	}

	
	@Override
	public boolean deleteUserById(Integer id) {
		userRepo.deleteById(id);
		return true;
	}

}
