/********************************************************
 *  
 *   Copyright 2021 On The Edge Software Consulting LLC.
 *   All rights reserved.
 *   
 */
package com.ontheedgesc.portal.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ontheedgesc.portal.business.UserBusinessService;
import com.ontheedgesc.portal.model.LoginModel;
import com.ontheedgesc.portal.model.RegisterModel;
import com.ontheedgesc.portal.model.UserModel;
import com.ontheedgesc.portal.util.DuplicateUserException;


/**
 * Login and Registration Controller
 * 
 * @author markreha
 *
 */
@Controller
@RequestMapping("/login-register")
public class LoginRegistrationController
{
	@Autowired
	UserBusinessService userBusinessService;
	
	/**
	 * Display the Log In View using URI of /.
	 * 
	 * @param model MVC Model
	 * @param httpSession HTTP session
	 * @return name of the Log In View
	 */
	@GetMapping("/")
	public String displayLogin(Model model, HttpSession httpSession)
	{
		// Invalidate the HTTP Session
		httpSession.invalidate();
		
		// Display Login Form View
		model.addAttribute("title", "Please Login");
		model.addAttribute("loginModel", new LoginModel());
		return "login";
	}

	/**
	 * Log out the user out and display the Log In View using URI of /logout.
	 * 
	 * @param model MVC Model
	 * @param httpSession HTTP session
	 * @return name of the Log In View
	 */
	@GetMapping("/logout")
	public String displayLogout(Model model, HttpSession httpSession)
	{
		// Invalidate the HTTP Session
		httpSession.invalidate();
		
		// Display Login Form View
		model.addAttribute("title", "Please Login");
		model.addAttribute("loginModel", new LoginModel());
		return "login";
	}

	/**
	 * Log In Success (from Spring Security configuration) using URI of /success.
	 * 
	 * @param model Login In Model
	 * @param httpSession HTTP session
	 * @return forward to the Main View
	 */
	@GetMapping("/success")
	public String loginSuccess(Model model, HttpSession httpSession)
	{
		// Get the User who is logged in
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();
		UserModel user = userBusinessService.getUserByUsername(currentPrincipalName);
		
        // Save the User in the HTTP Session
        httpSession.setAttribute("user", user);

        // Redirect to the Main View
        return "forward:/main/stacks";
	}

	/**
	 * Display the Registration View using URI of /register.
	 * 
	 * @param model MVC Model
	 * @return name of the Registration View
	 */
	@GetMapping("/register")
	public String displayRegister(Model model)
	{
		// Display Registration Form View
		model.addAttribute("title", "Register New User");
		model.addAttribute("registerModel", new RegisterModel());
		return "register";
	}

	/**
	 * Registration Form Post handler using URI of /doRegister.
	 * 
	 * @param registerModel Form Model
	 * @param bindingResult Validation binding results
	 * @param model MVC Model
	 * @return name of the Log In View
	 */
	@PostMapping("/doRegister")
	public String doRegister(@Valid RegisterModel registerModel, BindingResult bindingResult, Model model)
	{
        // Check for validation errors
        if (bindingResult.hasErrors()) 
        {
        	model.addAttribute("title", "Register New User");
            return "register";
        }
        
         // Register New User
        UserModel user = new UserModel(-1L, registerModel.getFirstName(), registerModel.getLastName(), registerModel.getEmailAddress(), registerModel.getUsername(), registerModel.getPassword(), false, true);
        try
		{
			userBusinessService.registerUser(user);
		} 
        catch (DuplicateUserException e)
		{
        	model.addAttribute("title", "Register New User");
        	bindingResult.addError(new ObjectError("username", "Please select another Username as this one already exists in the system."));
            return "register";
		}
        
 		// Display the Login View
		model.addAttribute("title", "Please Login");
		model.addAttribute("loginModel", new LoginModel());
		return "login";
	}
}


