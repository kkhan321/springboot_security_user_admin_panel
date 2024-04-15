package com.coder.Service;

import com.coder.dto.User;

public interface UserService {

	public User saveUser(User user);
	public void removeSessionMessage();
	public  boolean deleteUserById(Integer id) ;
		// TODO Auto-generated method stub
		
	
}
