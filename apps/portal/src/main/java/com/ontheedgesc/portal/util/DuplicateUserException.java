/********************************************************
 *  
 *   Copyright 2021 On The Edge Software Consulting LLC.
 *   All rights reserved.
 *   
 */
package com.ontheedgesc.portal.util;

/**
 * Custom Duplicate User Exception
 * 
 * @author markreha
 *
 */
public class DuplicateUserException extends Exception
{
	private static final long serialVersionUID = 1L;
	
	/**
	 * Default constructor
	 * 
	 */
	public DuplicateUserException()
	{
		super();
	}
	
	/**
	 * Non-default constructor
	 * 
	 * @param err Wrapped exception
	 * @param errorMessage Custome excetion message
	 */
	public DuplicateUserException(Throwable err, String errorMessage)
	{
		super(errorMessage, err);
	}
}
