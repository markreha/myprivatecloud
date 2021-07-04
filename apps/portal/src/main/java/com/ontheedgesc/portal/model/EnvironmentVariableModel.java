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
 * Environment Variables Application Model
 * 
 * @author markreha
 *
 */
@AllArgsConstructor
@Getter @Setter
public class EnvironmentVariableModel
{
	private Long id;
	private String name;
	private String value;
}
