/********************************************************
 *  
 *   Copyright 2021 On The Edge Software Consulting LLC.
 *   All rights reserved.
 *   
 */
package com.ontheedgesc.portal.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * User Application Model
 * 
 * @author markreha
 *
 */
@AllArgsConstructor
@Getter @Setter
public class UserModel
{
	private Long id;
	private String firstName;
	private String lastName;
	private String emailAddress;
	private String username;
	private String password;
	private boolean isAdmin;
	private boolean isActive;
}
