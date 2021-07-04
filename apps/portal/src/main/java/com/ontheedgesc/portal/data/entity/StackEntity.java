/********************************************************
 *  
 *   Copyright 2021 On The Edge Software Consulting LLC.
 *   All rights reserved.
 *   
 */
package com.ontheedgesc.portal.data.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


/**
 * Stack Database Entity Model
 * 
 * @author markreha
 *
 */
@Table("STACKS")
@AllArgsConstructor
@Getter @Setter
public class StackEntity
{
	@Id
	Long id;
	
	@Column("SHORT_NAME")
	String shortName;
	
	@Column("LONG_NAME")
	String longName;
	
	@Column("DESCRIPTION")
	String description;
	
	@Column("IMAGE")
	byte[] image;

	@Column("DOCKER_IMAGE")
	String dockerImage;

	@Column("POD_PATH")
	String podPath;
	
	@Column("CATEGORY")
	String category;

	@Column("TARGET_PORT")
	int targetPort;

	@Column("PUBLISH_PORT")
	int publishPort;

	@Column("CAN_DEPLOY_CODE")
	boolean canDeployCode;

	@Column("PV_CLAIM_FK")
	int pvClaimId;
}
