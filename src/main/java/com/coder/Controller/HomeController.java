package com.coder.Controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.coder.Repository.UserRepo;
import com.coder.Service.UserService;
import com.coder.Service.UserServiceImpl;
import com.coder.dto.User;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepo  repo;

	@Autowired
	private BCryptPasswordEncoder encoder;

	@Autowired
	private UserServiceImpl serviceImpl;

	@GetMapping("/")
	public String index() {
		return "index";
	}
	@GetMapping("/register")
	public String register() {
		return "register";
	}
	@GetMapping("/signin")
	public String login() {
		return "login";
	}

	@GetMapping("/user/changepassword")
	public String changepassword() {
		return "ChangePassword";
	}

	@GetMapping("/user/profile")
	public String profile(Principal p,Model model) {//throgh principle we will get whichever user is logining in we will get his email id
		String email=p.getName();
		User user=repo.findByEmail(email);
		model.addAttribute("user",user);
		return "profile";
	}
	@GetMapping("/user/home")
	public String home() {
		return "home";
	}

	@GetMapping("/admin/home")
	public String home1() {
		return "adminhome";
	}

	@PostMapping("/saveUser")
	public String saveUser(@ModelAttribute User user,HttpSession httpSession) {
		System.out.println(user);
		String pass =encoder.encode(user.getPassword());
		user.setPassword(pass);
		user.setRole("ROLE_USER");
		User user2=userService.saveUser(user);

		if(user2!=null) {
			httpSession.setAttribute("msg", "Register succesfully");
		}
		else {
			httpSession.setAttribute("msg", "something went wrong");
		}
		return "redirect:/register";
	}

	@ModelAttribute
	public void commUser(Principal p,Model m) {
		if(p!=null) {
			String email=p.getName();
			User user=repo.findByEmail(email);
			m.addAttribute("user",user);
		}

	}

	@PostMapping("/changePassword")
	public String changePassword(Principal p, @RequestParam("oldPass") String oldPass, 
			@RequestParam("newPass") String newPass,HttpSession session) {

		String email = p.getName();
		User myUser = repo.findByEmail(email);

		boolean f = encoder.matches(oldPass, myUser.getPassword());

		if(f) {
			myUser.setPassword(encoder.encode(newPass));
			User updatePassUser=serviceImpl.saveUser(myUser);
			if(updatePassUser!=null) {
				session.setAttribute("msg", "password is changed succesfully");
			}
			else {
				session.setAttribute("emsg", "somethong went wrong");

			}
		}
		else {

			session.setAttribute("emsg", "your old password is not match");

			System.out.println("Wrong Old Password");
		}

		return "redirect:/user/changepassword";
	}

	@GetMapping("/user/editUser")
	public String editUserPage() {
		return "editprofile";
	}

	@PostMapping("/updateUser")
	public String editUser(Principal p, @RequestParam("name") String name,
			@RequestParam("email") String email, @RequestParam("mobileNo") String phone, HttpSession session){

		String pEmail = p.getName();
		User myUser = repo.findByEmail(pEmail);

		myUser.setName(name);
		myUser.setEmail(email);
		myUser.setMobileNo(phone);

		User updatedUser = serviceImpl.saveUser(myUser);

		if (updatedUser != null) {
			session.setAttribute("msg", "Profile Details Updated Successfully...!");
			System.out.println("--->"+updatedUser);
		}
		else {
			session.setAttribute("emsg", "Something went wrong");
			System.out.println("--->"+"not successss");

		}
		return "profile";

	}

	@GetMapping("/admin/deleteUser/{id}")
	public String deleteUser(@PathVariable(value = "id") Integer id, HttpSession session) {
		userService.deleteUserById(id);
		session.setAttribute("msg", "User is Deleted Successfully..!");
		return "redirect:/admin/userList";
	}

	@GetMapping("/admin/userList")
	public String Userlist(Model model) {	
		model.addAttribute("userList",repo.findAll());
		return "userlist";
	}

}
