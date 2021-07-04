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
 * PVC Claim Application Model
 * 
 * @author markreha
 *
 */
@AllArgsConstructor
@Getter @Setter
public class PvClaimModel
{
	private Long id;
	private String friendlyName;
	private String actualName;
}
