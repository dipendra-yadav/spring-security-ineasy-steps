package com.demo.config;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@ComponentScan(basePackages = "com.demo.config")
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	DataSource dataSource;

	@Autowired
	public void configure(AuthenticationManagerBuilder auth) throws Exception {		
		auth.jdbcAuthentication().dataSource(dataSource)
		.passwordEncoder(passwordEncoder())
		  .usersByUsernameQuery(
		   "select username,password,enabled from users where username=?")
		  .authoritiesByUsernameQuery(
		   "select username,role from user_roles where username=?");
		//auth.authenticationProvider(authProvider());
	}
	
	@Bean
	public PasswordEncoder passwordEncoder(){
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder;
	}
	
	@Bean
	public UserDetailsService customUserDetailsService(){
		JdbcDaoImpl jdbcUserDetailService=new JdbcDaoImpl();
		String authQuery="select username,role from user_roles where username=?";
		String userQuery="select username,password,enabled from users where username=?";
		jdbcUserDetailService.setDataSource(dataSource);
		jdbcUserDetailService.setAuthoritiesByUsernameQuery(authQuery);
		jdbcUserDetailService.setUsersByUsernameQuery(userQuery);
		return jdbcUserDetailService;
	}
	 
	@Bean
	public DaoAuthenticationProvider authProvider() {
	    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
	    authProvider.setUserDetailsService(customUserDetailsService());
	    authProvider.setPasswordEncoder(passwordEncoder());
	    return authProvider;
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.authorizeRequests()
				.regexMatchers("/chief/.*").hasRole("CHIEF")
				.regexMatchers("/agent/.*").access("hasRole('AGENT') and principal.name='James Bond'")
				.antMatchers("/signup**").permitAll()
				.anyRequest().authenticated()
				.and().httpBasic()
				.and().requiresChannel().anyRequest().requiresSecure();
				
		http.formLogin().loginPage("/login").permitAll();		
	}
		
}
