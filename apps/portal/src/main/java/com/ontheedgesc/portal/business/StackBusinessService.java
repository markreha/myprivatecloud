/********************************************************
 *  
 *   Copyright 2021 On The Edge Software Consulting LLC.
 *   All rights reserved.
 *   
 */
package com.ontheedgesc.portal.business;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ontheedgesc.kubernetesapi.KubernetesApi;
import com.ontheedgesc.kubernetesapi.ServiceInfo;
import com.ontheedgesc.kubernetesapi.StorageInfo;
import com.ontheedgesc.portal.data.entity.PvClaimEntity;
import com.ontheedgesc.portal.data.entity.StackEntity;
import com.ontheedgesc.portal.data.entity.StackSizeEntity;
import com.ontheedgesc.portal.data.repository.StackRepository;
import com.ontheedgesc.portal.data.repository.StackSizeRepository;
import com.ontheedgesc.portal.data.repository.UserApplicationRepository;
import com.ontheedgesc.portal.data.entity.UserApplicationEntity;
import com.ontheedgesc.portal.model.PvClaimModel;
import com.ontheedgesc.portal.model.StackModel;
import com.ontheedgesc.portal.model.StackSizeModel;
import com.ontheedgesc.portal.util.DatabaseException;
import com.ontheedgesc.portal.util.ResourceLoader;
import com.ontheedgesc.portal.util.StacksException;

import lombok.extern.slf4j.Slf4j;

/**
 * Stacks Business Services
 * 
 * @author markreha
 *
 */
@Service
@Slf4j
public class StackBusinessService
{
	@Autowired
	private StackRepository stackRepository;
	
	@Autowired
	private StackSizeRepository stackSizeRepository;
	
	@Autowired
	private UserApplicationRepository userApplicationRepository;

	@Autowired
	private KubernetesApi api;


	/**
	 * Get all Stacks from the database.
	 * 
	 * @return List of Application Model Stacks
	 * @throws DatabaseException if something went wrong interacting with the database
	 */
	public List<StackModel> getAllStacks()
	{
		List<StackModel>stacks = new ArrayList<StackModel>();
    	try
		{
			// Get all all the Entity Stacks
			Iterable<StackEntity> stacksIterator = stackRepository.findAll();
			
			// Iterate over the Entity Model Users and create a list of Application Model Stacks
			for(StackEntity stack : stacksIterator)
			{
				PvClaimEntity pvClaimEntity = stackRepository.getPvClaim(stack.getPvClaimId());
				PvClaimModel pvClaim = new PvClaimModel(pvClaimEntity.getId(), pvClaimEntity.getFriendlyName(), pvClaimEntity.getActualName());
				stacks.add(new StackModel(stack.getId(), stack.getShortName(), stack.getLongName(), stack.getDescription(), stack.getImage(), stack.getDockerImage(), stack.getPodPath(), stack.getCategory(), stack.getTargetPort(), stack.getPublishPort(), stack.isCanDeployCode(), pvClaim));
			}
		} 
    	catch (Exception e)
		{
			// Log everyting
			log.info("Could not get all the stacks from the database");
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			log.info("Exception Stack Trace is" + sw.toString());
	    	
	    	// Throw a custom unchecked Database Exception back to the caller
	    	throw new DatabaseException(e, "Database exception");
		}
		
		// Return list of Application Model Stacks
		return stacks;
	}
	
	/**
	 * Get all the Stack Sizes from the database.
	 * 
	 * @return List of Application Model Stack Sizes
	 * @throws DatabaseException if something went wrong interacting with the database
	 */
	public List<StackSizeModel> getAllStackSizes()
	{
		List<StackSizeModel>stackSizes = new ArrayList<StackSizeModel>();
		try
		{
			// Get all the Stack Sizes
			Iterable<StackSizeEntity> stackSizesIterator = stackSizeRepository.findAll();
			
			// Iterate over the Entity Model Users and create a list of Application Model Stacks
			for(StackSizeEntity stackSize : stackSizesIterator)
			{
				stackSizes.add(new StackSizeModel(stackSize.getId(), stackSize.getName(), stackSize.getCpu(), stackSize.getMemory(), stackSize.getReplicas()));
			}
		} 
		catch (Exception e)
		{
			// Log everyting
			log.info("Could not get all the stack sizes from the database");
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			log.info("Exception Stack Trace is" + sw.toString());
	    	
	    	// Throw a custom unchecked Database Exception back to the caller
	    	throw new DatabaseException(e, "Database exception");
		}
		
		// Return list of Application Model Stack Sizes
		return stackSizes;
	}
	
	/**
	 * Provision a Stack to the Kubernetes cluster.
	 * 
	 * @param userId The User ID for this Stack
	 * @param appName Logical Name for this Application
	 * @param stackId The Stack ID to add
	 * @param stackSizeId The Stack Size for this Stack
	 * @throws StacksException if something bad happened
	 */
	public void provisionStack(Long userId, String appName, int stackId, int stackSizeId) throws StacksException
	{
		try
		{
			// Get the Stack, PV Claim, and Stack Sizes
			Optional<StackEntity> stack = stackRepository.findById(Long.valueOf(stackId));
			PvClaimEntity claim = stackRepository.getPvClaim(stack.get().getPvClaimId());
			Optional<StackSizeEntity> stackSize = stackSizeRepository.findById(Long.valueOf(stackSizeId));
			
			// Setup all the Kubernetes API Models
			//  Service Info
			String userID = String.valueOf(userId);
			String longName = stack.get().getLongName();
			String shortName = stack.get().getShortName().toLowerCase();
			String dockerImageName = stack.get().getDockerImage();
			boolean canDeployCodeTo = stack.get().isCanDeployCode();
			String serviceName = shortName + userID + "-" + System.currentTimeMillis();
			String namespace = "default";
			HashMap<String, String> labels = new HashMap<String, String>();
				labels.put("app", shortName);
				labels.put("environment", "pi");
				labels.put("owner", userID);
			HashMap<String, String> envs = new HashMap<String, String>();
			float cpus = stackSize.get().getCpu();
			int memMBytes = stackSize.get().getMemory();
			int replicas = stackSize.get().getReplicas();
			int targetPort = stack.get().getTargetPort();
			int publishPort = stack.get().getPublishPort();
			boolean useProxy = canDeployCodeTo;
			//  Storage Info
			String podPath = stack.get().getPodPath();
			String pvSubPath = serviceName;
			String pvClaim = claim.getActualName(); 				
					
			// Initialize all the Service Info Object Models
			StorageInfo storage = new StorageInfo(shortName + "-pv", podPath, pvSubPath, pvClaim);
			ServiceInfo info = new ServiceInfo(dockerImageName, serviceName, namespace, labels, envs, cpus, memMBytes, replicas, targetPort, publishPort, storage, useProxy);
			
			// Create a Kubernetes Service
			log.info("Creating Kubernetes Service............" + longName);
			io.fabric8.kubernetes.api.model.Service service = api.createService(info);
			
			// Copy the default Web Page to the Service Pod
	    	if(canDeployCodeTo)
	    	{
				File htmlFile = ResourceLoader.getAsFile("static/index-default-app.html");
	   			api.copyFileToPod(serviceName, "default", htmlFile, podPath + "/" + serviceName + "/" + "index.html");
	    	}
			
			// Create the App in the Database
	    	String appHostUrl = "";
	    	String appUrl = "";
	    	if(useProxy)
	    		appHostUrl = api.getMasterIpAddress() + ":" + api.getProxyPort();
			else
				appHostUrl = api.getMasterIpAddress() + ":" + service.getSpec().getPorts().get(0).getNodePort(); 
	    	if(canDeployCodeTo)
	    		appUrl = appHostUrl + "/" + serviceName + "/";
	    	else
	    		appUrl = appHostUrl;
	    	UserApplicationEntity app = new UserApplicationEntity(null, appName, userId, stackId, stackSizeId, appUrl, serviceName);
	    	userApplicationRepository.save(app);
		}
		catch(FileNotFoundException e1)
		{
			// Log everyting
			log.info("Could not load index.html file as a resource");
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e1.printStackTrace(pw);
			log.info("Exception Stack Trace is" + sw.toString());
	    	
	    	// Throw a custom checked Exception back to the caller
	    	throw new StacksException(e1, "Could not load index.html file as a resource");
		}
		catch(Exception e2)
		{
			// Log everyting
			log.info("Kubernetes Service API failed");
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e2.printStackTrace(pw);
			log.info("Exception Stack Trace is" + sw.toString());
	    	
	    	// Throw a custom checked Stacks Exception back to the caller
	    	throw new StacksException(e2, "Kubernetes Service API failed");
		}		
	}
	
	/**
	 * Deploy code to the Application.
	 * 
	 * @param appId Appplication ID
	 * @param fileToUpload File to upload/deploy
	 * @throws StacksException if something bad happened
	 */
	public void deployCodeToApp(String appId, File fileToUpload) throws StacksException
	{
		try
		{
			// Call Kubernetes API to do all the work
			api.deployCodeToPod(appId, "default", fileToUpload);
		} 
		catch (Exception e)
		{
			// Log everyting
			log.info("Kubernetes Service API failed");
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			log.info("Exception Stack Trace is" + sw.toString());
	    	
	    	// Throw a custom checked Stacks Exception back to the caller
	    	throw new StacksException(e, "Kubernetes Service API failed");
		}
	}

	/**
	 * Delete the Application.
	 * 
	 * @param id Portal Appplication ID
	 * @param appId Appplication ID
	 * @throws StacksException if something bad happened
	 */
	public void deleteApp(int id, String appId) throws StacksException
	{
		try
		{
			// Call Kubernetes API to do all the work
			boolean ok = api.deleteDeployment(appId, "default");
			
			// Delete the Application from the database
			if(ok)
				userApplicationRepository.deleteById(Long.valueOf(id));
		} 
		catch (Exception e)
		{
			// Log everyting
			log.info("Kubernetes Service API failed");
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			log.info("Exception Stack Trace is" + sw.toString());
	    	
	    	// Throw a custom checked Stacks Exception back to the caller
	    	throw new StacksException(e, "Kubernetes Service API failed");
		}
	}
}
