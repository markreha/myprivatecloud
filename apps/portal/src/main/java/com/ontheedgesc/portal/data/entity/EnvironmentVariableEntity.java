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
 * Environment Variable Database Entity Model
 * 
 * @author markreha
 *
 */
@Table("ENV_VARIABLES")
@AllArgsConstructor
@Getter @Setter
public class EnvironmentVariableEntity
{
	@Id
	Long id;
	
	@Column("STACK_FK")
	int stackId;
	
	@Column("NAME")
	String name;

	@Column("VALUE")
	String value;
}
