/********************************************************
 *  
 *   Copyright 2021 On The Edge Software Consulting LLC.
 *   All rights reserved.
 *   
 */
package com.ontheedgesc.portal.data.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * User Database Entity Model
 * 
 * @author markreha
 *
 */
@Table("USERS")
@AllArgsConstructor
@Getter @Setter
public class UserEntity
{
	@Id
	Long id;
	
	@Column("FIRST_NAME")
	String firstName;
	
	@Column("LAST_NAME")
	String lastName;

	@Column("EMAIL_ADDRESS")
	String emailAddress;
	
	@Column("USERNAME")
	String username;
	
	@Column("PASSWORD")
	String password;
	
	@Column("ADMIN")
	boolean isAdmin;

	@Column("ACTIVE")
	boolean isActive;
}
