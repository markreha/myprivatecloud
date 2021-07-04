/********************************************************
 *  
 *   Copyright 2021 On The Edge Software Consulting LLC.
 *   All rights reserved.
 *   
 */
package com.ontheedgesc.portal.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Appplication Provision Form Application Model
 * 
 * @author markreha
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class AppProvisionFormModel
{
	private int stackId;
	private String name;
	private int containerSize;
}
