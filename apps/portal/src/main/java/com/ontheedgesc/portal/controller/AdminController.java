/********************************************************
 *  
 *   Copyright 2021 On The Edge Software Consulting LLC.
 *   All rights reserved.
 *   
 */
package com.ontheedgesc.portal.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.ontheedgesc.portal.business.UserBusinessService;
import com.ontheedgesc.portal.model.UserModel;
import com.ontheedgesc.portal.util.DatabaseException;

/**
 * Admin Controller
 * 
 * @author markreha
 *
 */
@Controller
@RequestMapping("/admin")
public class AdminController
{
	@Autowired
	UserBusinessService userBusinessService;

	/**
	 * Display the Administration View using URI of /.
	 * 
	 * @param model MVC Model
	 * @param redirectError Optional redirect error message
	 * @return name of the Administration View
	 */
	@GetMapping("/")
	public String displayAdmin(@RequestParam(required=false, name="error") String redirectError, Model model)
	{
		boolean error = false;
		
		// Get all the Users
		List<UserModel>users = new ArrayList<UserModel>();
		try
		{
			users = userBusinessService.getAllUsers();
		} 
		catch (DatabaseException e)
		{
			error = true;
		}

		// Display Administration View
		model.addAttribute("title", "Administration");
		if(redirectError != null)
			model.addAttribute("error", redirectError);	
		else
			model.addAttribute("error", error ? "Error: See your My Private Cloud Administrator. Getting all the users failed." : "");
		model.addAttribute("users", users);
		return "administration";
	}

	/**
	 * Suspend or Activate User Post Form handler using URI of /doSuspendActivate.
	 * 
	 * @param userId User ID to suspend
	 * @param action If 1 then suspend else activate
	 * @param redirectAttributes Redirect Attributes for MVC redirect request
	 * @param model MVC Model
	 * @return redirect to the Administration View
	 */
	@PostMapping("/doSuspendActivate")
	public RedirectView doSuspend(@RequestParam("id") String userId, @RequestParam("action") String action, RedirectAttributes redirectAttributes, Model model) 
	{
		boolean error = false;
		
		// Suspend the User
		try
		{
			boolean ok = userBusinessService.suspendActivateUser(Long.valueOf(userId), action.equals("1"));
			if(!ok)
				error = true;
		} 
		catch (Exception e)
		{
			error = true;
		}
		
		// Display My Apps View
		redirectAttributes.addAttribute("error", error ? "Error: See your My Private Cloud Administrator. Suspending the user failed." : "");
		return new RedirectView("/admin/");
	}
}


