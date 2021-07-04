/********************************************************
 *  
 *   Copyright 2021 On The Edge Software Consulting LLC.
 *   All rights reserved.
 *   
 */
package com.ontheedgesc.portal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Root Controller
 * 
 * @author markreha
 *
 */
@Controller
@RequestMapping("/")
public class RootController
{
	/**
	 * Display the Log In View using URI of /.
	 * 
	 * @param model MVC Model
	 * @return forward to the Log In View
	 */
	@GetMapping("/")
	public String displayLogin(Model model)
	{
		return "forward:/login-register/";
	}
}
