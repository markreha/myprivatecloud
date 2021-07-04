/********************************************************
 *  
 *   Copyright 2021 On The Edge Software Consulting LLC.
 *   All rights reserved.
 *   
 */
package com.ontheedgesc.portal.util;

/**
 * Custom Database Exception
 * 
 * @author markreha
 *
 */
public class DatabaseException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor
	 * 
	 */
	public DatabaseException()
	{
		super();
	}
	
	/**
	 * Non-default constructor
	 * 
	 * @param err Wrapped exception
	 * @param errorMessage Custome excetion message
	 */
	public DatabaseException(Throwable err, String errorMessage)
	{
		super(errorMessage, err);
	}
}
