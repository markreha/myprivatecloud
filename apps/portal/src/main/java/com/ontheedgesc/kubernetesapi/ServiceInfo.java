/********************************************************
 *  
 *   Copyright 2021 On The Edge Software Consulting LLC.
 *   All rights reserved.
 *   
 */
package com.ontheedgesc.kubernetesapi;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Kubernetes Service Info Model
 * 
 * @author markreha
 *
 */
@AllArgsConstructor
@Getter @Setter
public class ServiceInfo
{
	private String image;					// The Image Name
	private String name;					// The Service Name
	private String namespace;				// The Service Namespace
	private Map<String, String> labels;		// Service Labels
	private Map<String, String> envs;		// Environment Variables
	private float cpus;						// The CPU's in a percent of a core, this is converted to millicores internally (multiplied by 1000 in the getter)
	private int memMBytes;					// The Memory in Mbytes
	private int replicas;					// The number of replicas to create in the Swarm
	private int targetPort;					// The Target Port inside the Container
	private int publishPort;				// The Published Port outside the Container
	private StorageInfo storageInfo;		// The Storage Info
	private boolean useIngress;				// Set to false for NodePort else true to use Reverse Proxy

	/**
	 * Return calculated CPU's.
	 * 
	 * @return Stored CPU's * 1000 (millicores)
	 */
	public float getCpus()
	{
		return cpus * 1000;
	}
}
