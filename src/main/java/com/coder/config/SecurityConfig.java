package com.coder.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {

		return new BCryptPasswordEncoder();
	}

	@Bean
	public UserDetailsService detailsService() {
		return new CustomUserDetailService();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(detailsService());
		authenticationProvider.setPasswordEncoder(bCryptPasswordEncoder());
		return authenticationProvider;
	}

	/*
	 * @Bean public SecurityFilterChain filterChain(HttpSecurity http) throws
	 * Exception{ http.csrf(AbstractHttpConfigurer::disable)
	 * .authorizeHttpRequests(registry->{
	 * registry.requestMatchers("/**").permitAll();
	 * registry.requestMatchers("/user/**").authenticated().and()
	 * .formLogin().loginPage(null)
	 * 
	 * })
	 * 
	 * retu rn http.build(); }
	 */

	
	//this is for single login like user login
	/*
	 * @Bean public SecurityFilterChain filterChain(HttpSecurity http) throws
	 * Exception { http.csrf(AbstractHttpConfigurer::disable).authorizeHttpRequests(
	 * (requests) ->
	 * requests.requestMatchers("/*")requests.requestMatchers("/","/register",
	 * "/signin","/saveUser") .permitAll()
	 * .requestMatchers("/user/**").authenticated()) .formLogin((form) ->
	 * form.loginPage("/signin").loginProcessingUrl("/userLogin")
	 * .defaultSuccessUrl("/user/profile").permitAll()); return http.build(); }
	 */
	
	@Bean
	public SecurityFilterChain chain(HttpSecurity http) throws Exception{
		
		return http.csrf(AbstractHttpConfigurer::disable).authorizeHttpRequests(registry->{
			registry.requestMatchers("/*").permitAll();//home page sabhko avilable hai
			registry.requestMatchers("/admin/**").hasRole("ADMIN");//agar yeaah page acces karne jaayenge tohh bydefault login page aayenga
			registry.requestMatchers("/user/**").hasRole("USER");
			registry.anyRequest().authenticated();
		})
		.formLogin(httpSecurityFormLoginConfigurer->{
			httpSecurityFormLoginConfigurer.loginPage("/signin").loginProcessingUrl("/userLogin")
			.successHandler(new CustomAuthSuccesHandler())
			.permitAll();
		})	
	    .build();
	}
	
	
	
	/*
	 * @Bean public SecurityFilterChain chain1(HttpSecurity http) throws Exception {
	 * return http.csrf(AbstractHttpConfigurer::disable)
	 * .authorizeHttpRequests(registry -> {
	 * registry.requestMatchers("/*").permitAll();//home page sabhko available hai
	 * registry.requestMatchers("/admin/**").hasRole("ADMIN");//agar yeh page access
	 * karne jayenge toh bydefault login page aayenga
	 * registry.requestMatchers("/user/**").hasRole("USER");
	 * registry.anyRequest().authenticated(); })
	 * .formLogin(httpSecurityFormLoginConfigurer -> {
	 * httpSecurityFormLoginConfigurer.loginPage("/signin") .successHandler(new
	 * CustomAuthSuccessHandler()) .permitAll(); }) .build(); }
	 */
	
}
