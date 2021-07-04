/********************************************************
 *  
 *   Copyright 2021 On The Edge Software Consulting LLC.
 *   All rights reserved.
 *   
 */
package com.ontheedgesc.portal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ontheedgesc.portal.business.SecurityBusinessService;


/**
 * Spring Security Configuration
 * 
 * @author markreha
 *
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter 
{
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	SecurityBusinessService service;

	@Bean
	BCryptPasswordEncoder passwordEncoder()
	{
	    return new BCryptPasswordEncoder();
	}	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception 
	{
		http.csrf().disable()
			.authorizeRequests()
			.antMatchers("/", "/images/**", "/css/**", "/login-register/register/**", "/login-register/doRegister/**").permitAll()
				.anyRequest().authenticated()
				.and()
			.formLogin()
				.loginPage("/login-register/")
				.usernameParameter("username")
                .passwordParameter("password")
				.permitAll()
				.defaultSuccessUrl("/login-register/success", true)
				.and()
			.logout()
				.logoutUrl("/login-register/logout")
				.invalidateHttpSession(true)
				.clearAuthentication(true)
				.permitAll()
				.logoutSuccessUrl("/");
	}

    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception
    {
//    	String password = new BCryptPasswordEncoder().encode("test");
//    	System.out.println("================================> " + password);

    	auth
        .userDetailsService(service)
        .passwordEncoder(passwordEncoder); 
    }
}

