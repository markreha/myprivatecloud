/********************************************************
 *  
 *   Copyright 2021 On The Edge Software Consulting LLC.
 *   All rights reserved.
 *   
 */
package com.ontheedgesc.portal.model;

import org.apache.commons.codec.binary.Base64;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Stack Application Model
 * 
 * @author markreha
 *
 */
@AllArgsConstructor
@Getter @Setter
public class StackModel
{
	private Long id;
	private String shortName;
	private String longName;
	private String description;
	private byte[] image;
	private String dockerImage;
	private String podPath;
	private String category;
	private int targetPort;
	private int publishPort;
	private boolean canDeployCode;
	private PvClaimModel pvClaim;

	/**
	 * Getter method to get a Base 64 Encoded version of the image.
	 * 
	 * @return Base 64 Encoded version of the image
	 */
	public String getImage64()
	{
		return Base64.encodeBase64String(this.image);
	}
}
