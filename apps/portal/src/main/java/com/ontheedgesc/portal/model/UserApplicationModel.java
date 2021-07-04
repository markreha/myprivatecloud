/********************************************************
 *  
 *   Copyright 2021 On The Edge Software Consulting LLC.
 *   All rights reserved.
 *   
 */
package com.ontheedgesc.portal.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * User Provisioned Application Application Model
 * 
 * @author markreha
 *
 */
@AllArgsConstructor
@Getter @Setter
public class UserApplicationModel
{
	private Long id;
	private String name;
	private Long userId;
	private StackModel stack;
	private StackSizeModel stackSize;
	private List<EnvironmentVariableModel> envVariables; 
	private String appUrl;
	private String appId;
}
