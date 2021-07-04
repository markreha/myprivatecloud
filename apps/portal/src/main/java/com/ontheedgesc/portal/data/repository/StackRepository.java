/********************************************************
 *  
 *   Copyright 2021 On The Edge Software Consulting LLC.
 *   All rights reserved.
 *   
 */
package com.ontheedgesc.portal.data.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.ontheedgesc.portal.data.entity.PvClaimEntity;
import com.ontheedgesc.portal.data.entity.StackEntity;

/**
 * Stack Database Repository Pattern
 * 
 * @author markreha
 *
 */
public interface StackRepository extends CrudRepository<StackEntity, Long>
{
	/**
	 * Custom query to find PV Claim
	 * 
	 * @param id PV Claim ID to find
	 * @return A PvClaimEntity
	 */
  @Query("SELECT * from PV_CLAIMS where id = :id")
   PvClaimEntity getPvClaim(@Param("id") int id);
}
