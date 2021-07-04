/********************************************************
 *  
 *   Copyright 2021 On The Edge Software Consulting LLC.
 *   All rights reserved.
 *   
 */
package com.ontheedgesc.portal.business;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ontheedgesc.portal.data.entity.UserEntity;
import com.ontheedgesc.portal.data.repository.UserRepository;

/**
 * Security Business Services
 * 
 * @author markreha
 *
 */
@Service
public class SecurityBusinessService implements UserDetailsService
{
	@Autowired
	private UserRepository userRepository;
	
	/**
	 * This method is overridden from the base class and is used to support Spring Security user authentication
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException 
	{
		// Try to find the User in the database. If not found throw a User Not Found exception else return a Spring Security User.
	    UserEntity user = userRepository.findByUsername(username);
	    if(user != null && user.isActive()) 
	    {
	    	List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
	    	authorities.add(new SimpleGrantedAuthority("USER"));
	    	return new User(user.getUsername(), user.getPassword(), authorities);
	    } 
	    else 
	    {
	        throw new UsernameNotFoundException("username not found");
	    }
	}
}
