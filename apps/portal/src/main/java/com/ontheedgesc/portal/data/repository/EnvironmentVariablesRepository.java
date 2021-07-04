/********************************************************
 *  
 *   Copyright 2021 On The Edge Software Consulting LLC.
 *   All rights reserved.
 *   
 */
package com.ontheedgesc.portal.data.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.ontheedgesc.portal.data.entity.EnvironmentVariableEntity;

/**
 * Environment Variable Database Repository Pattern
 * 
 * @author markreha
 *
 */
public interface EnvironmentVariablesRepository extends CrudRepository<EnvironmentVariableEntity, Long>
{
	/**
	 * Finder by Stack ID
	 * 
	 * @param stackId Stack ID to find
	 * @return List of EnvironmentVariableEntity
	 */
	List<EnvironmentVariableEntity> findByStackId(int stackId);
}
