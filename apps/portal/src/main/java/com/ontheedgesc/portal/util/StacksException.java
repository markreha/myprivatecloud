/********************************************************
 *  
 *   Copyright 2021 On The Edge Software Consulting LLC.
 *   All rights reserved.
 *   
 */
package com.ontheedgesc.portal.util;

/**
 * Custom Kubernetes API Stacks Exception
 * 
 * @author markreha
 *
 */
public class StacksException extends Exception
{
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor
	 * 
	 */
	public StacksException()
	{
		super();
	}
	
	/**
	 * Non-default constructor
	 * 
	 * @param err Wrapped exception
	 * @param errorMessage Custome excetion message
	 */
	public StacksException(Throwable err, String errorMessage)
	{
		super(errorMessage, err);
	}
}
