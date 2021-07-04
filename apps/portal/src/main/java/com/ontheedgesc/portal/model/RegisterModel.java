/********************************************************
 *  
 *   Copyright 2021 On The Edge Software Consulting LLC.
 *   All rights reserved.
 *   
 */
package com.ontheedgesc.portal.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

/**
 * Register Form Application Model
 * 
 * @author markreha
 *
 */
@Getter @Setter
public class RegisterModel
{
	@NotNull(message="First Name is a required field")
	@Size(min=1, max=100, message="First Name must be between 1 and 100 characters")
	private String firstName;
	
	@NotNull(message="Last Name is a required field")
	@Size(min=1, max=100, message="Last Name must be between 1 and 100 characters")
	private String lastName;
	
	@NotNull(message="Email Address is a required field")
	@Size(min=1, max=100, message="Email Address must be between 1 and 100 characters")
	private String emailAddress;
	
	@NotNull(message="User name is a required field")
	@Size(min=1, max=45, message="User name must be between 1 and 45 characters")
	private String username;
	
	@NotNull(message="Password is a required field")
	@Size(min=1, max=100, message="Password must be between 1 and 100 characters")
	private String password;
}
