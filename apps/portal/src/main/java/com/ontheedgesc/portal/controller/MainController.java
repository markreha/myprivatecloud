/********************************************************
 *  
 *   Copyright 2021 On The Edge Software Consulting LLC.
 *   All rights reserved.
 *   
 */
package com.ontheedgesc.portal.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.ontheedgesc.portal.business.StackBusinessService;
import com.ontheedgesc.portal.model.AppProvisionFormModel;
import com.ontheedgesc.portal.model.StackModel;
import com.ontheedgesc.portal.model.StackSizeModel;
import com.ontheedgesc.portal.model.UserModel;
import com.ontheedgesc.portal.util.StacksException;

/**
 * Main Controller
 * 
 * @author markreha
 *
 */
@Controller
@RequestMapping("/main/")
public class MainController
{
	@Autowired
	StackBusinessService stackBusinessService;
	
	/**
	 * Display the Stacks View using URI of /stacks.
	 * 
	 * @param model MVC Model
	 * @return name of the Main View
	 */
	@GetMapping("/stacks")
	public String displayStacks(Model model)
	{
        // Get all the Stacks for displaying all the Stacks 
        List<StackModel> stacks = stackBusinessService.getAllStacks();
        
        // Get all the Stack Sizes for displaying all the Stacks
        List<StackSizeModel> stackSizes = stackBusinessService.getAllStackSizes();
        List<StackModel> htmls = new ArrayList<StackModel>();
        List<StackModel> webapps = new ArrayList<StackModel>();
        List<StackModel> databases = new ArrayList<StackModel>();
        List<StackModel> tools = new ArrayList<StackModel>();
        for(StackModel stack : stacks)
        {
        	if(stack.getCategory().equals("web"))
        		htmls.add(stack);
        	else if(stack.getCategory().equals("webapp"))
        		webapps.add(stack);
        	else if(stack.getCategory().equals("database"))
        		databases.add(stack);
        	else if(stack.getCategory().equals("tool"))
        		tools.add(stack);
        }
       
 		// Display the Main View
		model.addAttribute("title", "Cloud Services Catalog");
		model.addAttribute("htmlStacks", htmls);
		model.addAttribute("webappStacks", webapps);
		model.addAttribute("databaseStacks", databases);
		model.addAttribute("toolStacks", tools);
		model.addAttribute("stackSizes", stackSizes);
		model.addAttribute("formModel", new AppProvisionFormModel());
		return "main";
	}

	/**
	 * Provision Stack Post Form handler using URI of /doProvision.
	 * 
	 * @param model MVC Model 
	 * @param appProvisionModel Application Provision Form Model
	 * @param redirectAttributes Redirect attributes
	 * @param httpSession HTTP session
	 * @return redirect to My Applications View
	 */
	@PostMapping("/doProvision")
	public RedirectView doProvision(Model model, @ModelAttribute("formModel") AppProvisionFormModel appProvisionModel, RedirectAttributes redirectAttributes, HttpSession httpSession)
	{
		boolean error = false;
		
		// Provision the Application
		try
		{
			Long userId = ((UserModel)httpSession.getAttribute("user")).getId();
			stackBusinessService.provisionStack(userId, appProvisionModel.getName(), appProvisionModel.getStackId(), appProvisionModel.getContainerSize());
		} 
		catch (StacksException e)
		{
			error = true;
		}
		
		// Display My Apps View with results
		redirectAttributes.addAttribute("error", error ? "Error: See your My Private Cloud Administrator. Provisioning your application failed." : "");
		return new RedirectView("/myapps/");
	}
}


