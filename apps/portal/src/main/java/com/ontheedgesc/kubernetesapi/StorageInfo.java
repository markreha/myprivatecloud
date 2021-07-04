/********************************************************
 *  
 *   Copyright 2021 On The Edge Software Consulting LLC.
 *   All rights reserved.
 *   
 */
package com.ontheedgesc.kubernetesapi;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Kubernetes Storage Info Model
 * 
 * @author markreha
 *
 */
@AllArgsConstructor
@Getter @Setter
public class StorageInfo
{
	private String name;					// Storage Name
	private String podPath;					// The Path inside the Pod to map
	private String pvSubPath;				// The Sub-path off of the Persistent Volume
	private String pvClaimName;				// The Persistent Volume Claim Name
}
