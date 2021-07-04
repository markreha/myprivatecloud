/********************************************************
 *  
 *   Copyright 2021 On The Edge Software Consulting LLC.
 *   All rights reserved.
 *   
 */
package com.ontheedgesc.portal.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

/**
 * Global Exception Handler
 * 
 * @author markreha
 *
 */
@ControllerAdvice
public class GlobalDefaultExceptionHandler
{
	/**
	 * Controller method to handle the unhandled Exception.
	 * 
	 * @param ex The source Exception
	 * @return Model and name of the Exception View
	 */
	@ExceptionHandler(Exception.class)
	public ModelAndView handleException(Exception ex)
	{
		// Create a Model and View, populate with the Exception information, and display a common Error Page
		ModelAndView model = new ModelAndView();
		model.addObject("title", "Error Page");
		model.addObject("error", "Error: See your My Private Cloud Administrator. An Exception was not handled in the application: " + ex.getMessage());
		model.setViewName("exception");
		return model;
	}
}
