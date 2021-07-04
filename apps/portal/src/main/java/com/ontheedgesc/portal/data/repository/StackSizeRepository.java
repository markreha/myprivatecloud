/********************************************************
 *  
 *   Copyright 2021 On The Edge Software Consulting LLC.
 *   All rights reserved.
 *   
 */
package com.ontheedgesc.portal.data.repository;

import org.springframework.data.repository.CrudRepository;

import com.ontheedgesc.portal.data.entity.StackSizeEntity;

/**
 * Stack Size Database Repository Pattern
 * 
 * @author markreha
 *
 */
public interface StackSizeRepository extends CrudRepository<StackSizeEntity, Long>
{
}
