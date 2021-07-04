/********************************************************
 *  
 *   Copyright 2021 On The Edge Software Consulting LLC.
 *   All rights reserved.
 *   
 */
package com.ontheedgesc.portal.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.ontheedgesc.portal.business.StackBusinessService;
import com.ontheedgesc.portal.business.UserBusinessService;
import com.ontheedgesc.portal.model.AppDeleteFormModel;
import com.ontheedgesc.portal.model.AppDeploymentFormModel;
import com.ontheedgesc.portal.model.UserApplicationModel;
import com.ontheedgesc.portal.model.UserModel;

/**
 * My Apps Controller
 * 
 * @author markreha
 *
 */
@Controller
@RequestMapping("/myapps")
public class MyAppsController
{
	@Autowired
	UserBusinessService userBusinessService;

	@Autowired
	StackBusinessService stackBusinessService;

	/**
	 * Display the My Applications View using URI of /.
	 * 
	 * @param redirectError Optional redirect error message
	 * @param model MVC Model
	 * @param httpSession HTTP session
	 * @return name of My Applications View
	 */
	@GetMapping("/")
	public String displayApps(@RequestParam(required=false, name="error") String redirectError, Model model, HttpSession httpSession)
	{
		boolean error = false;

		// Get all the Users Applications
		List<UserApplicationModel> applications = new ArrayList<UserApplicationModel>();
		try
		{
			Long userId = ((UserModel)httpSession.getAttribute("user")).getId();
			applications = userBusinessService.getAllUserApplications(userId);
		} 
		catch (Exception e)
		{
			error = true;
		}
		
		// Display My Apps View
		model.addAttribute("title", "My Applications");
		if(redirectError != null)
			model.addAttribute("error", redirectError);			
		else
			model.addAttribute("error", error ? "Error: See your My Private Cloud Administrator. Getting your applications failed." : "");			
		model.addAttribute("apps", applications);
		return "myapps";
	}
	
	/**
	 * Deploy Code Post Form handler using URI of /doDeployment.
	 * 
	 * @param appDeploymentModel Deployment Form Post
	 * @param multipartFile File that was uploaded by the user
	 * @param redirectAttributes Redirect Attributes for MVC redirect request
	 * @param model MVC Model
	 * @return redirect to the My Apps View
	 */
	@PostMapping("/doDeployment")
	public RedirectView doDeployment(Model model, @ModelAttribute("formModel1") AppDeploymentFormModel appDeploymentModel, @RequestParam("file") MultipartFile multipartFile, RedirectAttributes redirectAttributes) 
	{
		boolean error = false;
		
		// Deploy the Application Code
		try
		{
			// Copy the Multipart File to a File created in the tmp directory
			File fileToUpload = new File(System.getProperty("java.io.tmpdir") + multipartFile.getOriginalFilename());
			OutputStream os = new FileOutputStream(fileToUpload);
			os.write(multipartFile.getBytes());
			os.close();
			
			// Upload File to Application
			stackBusinessService.deployCodeToApp(appDeploymentModel.getAppId(), fileToUpload);
			
			// Cleanup
			fileToUpload.delete();
		} 
		catch (Exception e)
		{
			error = true;
		}
		
		// Display My Apps View
		redirectAttributes.addAttribute("error", error ? "Error: See your My Private Cloud Administrator. Deploying code to your application failed." : "");
		return new RedirectView("/myapps/");
	}

	/**
	 * Delete Application Post Form handler using URI of /doDelete.
	 * 
	 * @param appDeleteModel Delete Form Post
	 * @param redirectAttributes Redirect Attributes for MVC redirect request
	 * @param model MVC Model
	 * @return redirect to the My Apps View
	 */
	@PostMapping("/doDelete")
	public RedirectView doDelete(Model model, @ModelAttribute("formModel2") AppDeleteFormModel appDeleteModel, RedirectAttributes redirectAttributes) 
	{
		boolean error = false;
		
		// Delete the Application Code
		try
		{
			// Delete the Application
			stackBusinessService.deleteApp(Integer.valueOf(appDeleteModel.getId()), appDeleteModel.getAppId());
		} 
		catch (Exception e)
		{
			error = true;
		}
		
		// Display My Apps View
		redirectAttributes.addAttribute("error", error ? "Error: See your My Private Cloud Administrator. Deleting your application failed." : "");
		return new RedirectView("/myapps/");
	}
}


