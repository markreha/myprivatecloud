/********************************************************
 *  
 *   Copyright 2021 On The Edge Software Consulting LLC.
 *   All rights reserved.
 *   
 */
package com.ontheedgesc.portal.data.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.ontheedgesc.portal.data.entity.UserApplicationEntity;

/**
 * User Provisioned Application Database Repository Pattern
 * 
 * @author markreha
 *
 */
public interface UserApplicationRepository extends CrudRepository<UserApplicationEntity, Long>
{
	/**
	 * Finder for User Provisiioned Applications
	 * 
	 * @param userId User ID to find
	 * @return List of UserApplicationEntity
	 */
	List<UserApplicationEntity> findByUserId(Long userId);
}
