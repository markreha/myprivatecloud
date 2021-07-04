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
 * Stack Size Application Model
 * 
 * @author markreha
 *
 */
@AllArgsConstructor
@Getter @Setter
public class StackSizeModel
{
	private Long id;
	private String name;
	private float cpu;
	private int memory;
	private int replicas;
}
