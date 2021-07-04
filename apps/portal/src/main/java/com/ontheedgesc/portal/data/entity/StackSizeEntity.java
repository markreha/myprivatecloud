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
 * Stack Size Database Entity Model
 * 
 * @author markreha
 *
 */
@Table("STACK_SIZES")
@AllArgsConstructor
@Getter @Setter
public class StackSizeEntity
{
	@Id
	Long id;
	
	@Column("NAME")
	String name;
	
	@Column("CPU")
	float cpu;
	
	@Column("MEM_MB")
	int memory;
	
	@Column("MAX_REPLICAS")
	int replicas;
}
