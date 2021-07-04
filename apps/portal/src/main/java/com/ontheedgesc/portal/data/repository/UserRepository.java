/********************************************************
 *  
 *   Copyright 2021 On The Edge Software Consulting LLC.
 *   All rights reserved.
 *   
 */
package com.ontheedgesc.portal.data.repository;

import org.springframework.data.repository.CrudRepository;

import com.ontheedgesc.portal.data.entity.UserEntity;

/**
 * User Database Repository Pattern
 * 
 * @author markreha
 *
 */
public interface UserRepository extends CrudRepository<UserEntity, Long>
{
	/**
	 * Finder by Username
	 * 
	 * @param username Username to find
	 * @return A UserEntity
	 */
	UserEntity findByUsername(String username);
}
