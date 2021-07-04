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
 * User Provisioned Application Database Entity Model
 * 
 * @author markreha
 *
 */
@Table("USER_APPLICATIONS")
@AllArgsConstructor
@Getter @Setter
public class UserApplicationEntity
{
	@Id
	Long id;
	
	@Column("NAME")
	String name;
	
	@Column("USER_ID_FK")
	Long userId;
	
	@Column("STACK_FK")
	int stackId;

	@Column("STACK_SIZE_FK")
	int stackSizeId;
	
	@Column("APP_URL")
	String appUrl;
	
	@Column("APP_ID")
	String appId;
}
