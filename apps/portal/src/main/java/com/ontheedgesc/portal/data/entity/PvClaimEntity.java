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
 * PV Claim Database Entity Model
 * 
 * @author markreha
 *
 */
@Table("PV_CLAIMS")
@AllArgsConstructor
@Getter @Setter
public class PvClaimEntity
{
	@Id
	Long id;
	
	@Column("FRIENDLY_NAME")
	String friendlyName;
	
	@Column("ACTUAL_NAME")
	String actualName;
}
