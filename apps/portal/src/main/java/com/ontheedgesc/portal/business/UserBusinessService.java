/********************************************************
 *  
 *   Copyright 2021 On The Edge Software Consulting LLC.
 *   All rights reserved.
 *   
 */
package com.ontheedgesc.portal.business;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ontheedgesc.portal.data.entity.EnvironmentVariableEntity;
import com.ontheedgesc.portal.data.entity.PvClaimEntity;
import com.ontheedgesc.portal.data.entity.StackEntity;
import com.ontheedgesc.portal.data.entity.StackSizeEntity;
import com.ontheedgesc.portal.data.entity.UserApplicationEntity;
import com.ontheedgesc.portal.data.entity.UserEntity;
import com.ontheedgesc.portal.data.repository.EnvironmentVariablesRepository;
import com.ontheedgesc.portal.data.repository.StackRepository;
import com.ontheedgesc.portal.data.repository.StackSizeRepository;
import com.ontheedgesc.portal.data.repository.UserApplicationRepository;
import com.ontheedgesc.portal.data.repository.UserRepository;
import com.ontheedgesc.portal.model.EnvironmentVariableModel;
import com.ontheedgesc.portal.model.PvClaimModel;
import com.ontheedgesc.portal.model.StackModel;
import com.ontheedgesc.portal.model.StackSizeModel;
import com.ontheedgesc.portal.model.UserApplicationModel;
import com.ontheedgesc.portal.model.UserModel;
import com.ontheedgesc.portal.util.DatabaseException;
import com.ontheedgesc.portal.util.DuplicateUserException;

import lombok.extern.slf4j.Slf4j;

/**
 * User, Login, and Registration Business Services
 * 
 * @author markreha
 *
 */
@Service
@Slf4j
public class UserBusinessService
{
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserApplicationRepository userApplicationRepository;

	@Autowired
	private StackRepository stackRepository;
	
	@Autowired
	private StackSizeRepository stackSizeRepository;

	@Autowired
	private EnvironmentVariablesRepository environmentVariableRepository;

	/**
	 * Get a User given their username
	 * @param username The User's username to find
	 * @return The User from the database
	 */
	public UserModel getUserByUsername(String username)
	{
		try
		{
			// Find User in the database
			UserEntity userEntity = userRepository.findByUsername(username);
			if(userEntity == null)
				return null;
			
			// From the User Entity create and return a User Model
			return new UserModel(userEntity.getId(), userEntity.getFirstName(), userEntity.getLastName(), userEntity.getEmailAddress(), userEntity.getUsername(), userEntity.getPassword(), userEntity.isAdmin(), userEntity.isActive());
		}
		catch (Exception e)
		{
			// Log everyting
			log.info("Could not find username in the database");
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			log.info("Exception Stack Trace is" + sw.toString());
	    	
	    	// Throw a custom unchecked Database Exception back to the caller
	    	throw new DatabaseException(e, "Database exception");
		}
	}
	
	/**
	 * Register a new User in the database.
	 * 
	 * @param user User to register
	 * @return true if all OK
	 * @throws DatabaseException if something went wrong interacting with the database
	 * @throws DuplicateUserException if username already exists in the database
	 */
    public boolean registerUser(UserModel user) throws DuplicateUserException
    {
    	try
		{
    		// See if username already exists
    		UserModel checkUser = getUserByUsername(user.getUsername());
    		if(checkUser != null)
    			throw new DuplicateUserException();
    		
			// Copy Application Model User to an User Entity Model
    		String encryptedPassword = new BCryptPasswordEncoder().encode(user.getPassword());
			UserEntity entity = new UserEntity(null, user.getFirstName(), user.getLastName(), user.getEmailAddress(), user.getUsername(),encryptedPassword, user.isAdmin(), user.isActive());
			
			// Insert new User
			userRepository.save(entity);
			
			// Return OK
			return true;
		} 
    	catch (DuplicateUserException e1)
    	{
			throw new DuplicateUserException();
    	}
    	catch (Exception e2)
		{
			// Log everyting
			log.info("Could not register user in the database");
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e2.printStackTrace(pw);
			log.info("Exception Stack Trace is" + sw.toString());
	    	
	    	// Throw a custom unchecked Database Exception back to the caller
	    	throw new DatabaseException(e2, "Database exception");
		}
    }
    
	/**
	 * Supsend or Activate a User in the database.
	 * 
	 * @param userId User ID to susend
	 * @param suspend If true then suspend User else activate the User
	 * @return true if all OK
	 * @throws DatabaseException if something went wrong interacting with the database
	 */
    public boolean suspendActivateUser(Long userId, boolean suspend)
    {
    	try
		{
			// Get the User
			Optional<UserEntity> userEntity = userRepository.findById(userId);
			if(userEntity.isEmpty())
				return false;
			
			// Suspend or Activate the User
			userEntity.get().setActive(!suspend);
			userRepository.save(userEntity.get());

			// Return OK
			return true;
		} 
    	catch (Exception e)
		{
			// Log everyting
			log.info("Could not suspend the user in the database");
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			log.info("Exception Stack Trace is" + sw.toString());
	    	
	    	// Throw a custom unchecked Database Exception back to the caller
	    	throw new DatabaseException(e, "Database exception");
		}
    }

    /**
     * Get all the Users.
     * 
     * @return List of Users
	 * @throws DatabaseException if something went wrong interacting with the database
     */
    public List<UserModel> getAllUsers()
    {
		List<UserModel>users = new ArrayList<UserModel>();

		try
		{
			// Get all the User Entities from the database
			Iterable<UserEntity> usersIterator = userRepository.findAll();
			
			// Iterate over all the User Entities and create a list of User Models
			for(UserEntity userEntity : usersIterator)
			{
				users.add(new UserModel(userEntity.getId(), userEntity.getFirstName(), userEntity.getLastName(), userEntity.getEmailAddress(), userEntity.getUsername(), userEntity.getPassword(), userEntity.isAdmin(), userEntity.isActive()));
			}
		} 
		catch (Exception e)
		{
			// Log everyting
			log.info("Could not get all the users from the database");
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			log.info("Exception Stack Trace is" + sw.toString());
	    	
	    	// Throw a custom unchecked Database Exception back to the caller
	    	throw new DatabaseException(e, "Database exception");
		}    	
		
		// Return all the Users
		return users;
    }
    
    /**
     * Get all the Users Provisioned Applications.
     * 
     * @param userId User ID to get applications for
     * @return List of Users Applications
	 * @throws DatabaseException if something went wrong interacting with the database
     */
    public List<UserApplicationModel> getAllUserApplications(Long userId)
    {
		List<UserApplicationModel>userApplications = new ArrayList<UserApplicationModel>();

		try
		{
			// Get all the User Application Entities from the database
			List<UserApplicationEntity> appsIterator = userApplicationRepository.findByUserId(userId);
			
			// Iterate over the Application Entity Models and get the Stack, Environment Variables, and Stack Size
			for(UserApplicationEntity appEntity : appsIterator)
			{
				// Get the Stack, Environment Variables, and Stack Size
				List<Long> stackIds = new ArrayList<Long>();
				stackIds.add(Long.valueOf(appEntity.getStackId()));
				List<EnvironmentVariableEntity> envVariablesEntity = new ArrayList<EnvironmentVariableEntity>();
				StackEntity stackEntity = stackRepository.findById(Long.valueOf(appEntity.getStackId())).get();
				StackSizeEntity stackSizeEntity = stackSizeRepository.findById(Long.valueOf(appEntity.getStackSizeId())).get();
				PvClaimEntity pvClaimEntity = stackRepository.getPvClaim(stackEntity.getPvClaimId());
				environmentVariableRepository.findByStackId(appEntity.getStackId()).forEach(envVariablesEntity::add);
				
				// Create an Application Model User Application from all the Entity Models
				List<EnvironmentVariableModel> envVariablesModel = new ArrayList<EnvironmentVariableModel>();
				PvClaimModel pvClaimModel = new PvClaimModel(pvClaimEntity.getId(), pvClaimEntity.getFriendlyName(), pvClaimEntity.getActualName());
				StackModel stackModel = new StackModel(stackEntity.getId(), stackEntity.getShortName(), stackEntity.getLongName(), stackEntity.getDescription(), stackEntity.getImage(), stackEntity.getDockerImage(), stackEntity.getPodPath(), stackEntity.getCategory(), stackEntity.getTargetPort(), stackEntity.getPublishPort(), stackEntity.isCanDeployCode(), pvClaimModel);
				StackSizeModel stackSizeModel = new StackSizeModel(stackSizeEntity.getId(), stackSizeEntity.getName(), stackSizeEntity.getCpu(), stackSizeEntity.getMemory(), stackSizeEntity.getReplicas());
				for(EnvironmentVariableEntity env : envVariablesEntity)
				{
					envVariablesModel.add(new EnvironmentVariableModel(env.getId(), env.getName(), env.getValue()));
				}
				UserApplicationModel userApplication = new UserApplicationModel(appEntity.getId(), appEntity.getName(), userId, stackModel, stackSizeModel, envVariablesModel, appEntity.getAppUrl(), appEntity.getAppId());
				
				// Add to the list of Application Model User Applications
				userApplications.add(userApplication);
			}
		} 
		catch (Exception e)
		{
			// Log everyting
			log.info("Could not get all the users applications from the database");
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			log.info("Exception Stack Trace is" + sw.toString());
	    	
	    	// Throw a custom unchecked Database Exception back to the caller
	    	throw new DatabaseException(e, "Database exception");
		}    	
		
		// Return all the Users Applications
		return userApplications;
    }
}
