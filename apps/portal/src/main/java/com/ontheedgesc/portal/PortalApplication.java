/********************************************************
 *  
 *   Copyright 2021 On The Edge Software Consulting LLC.
 *   All rights reserved.
 *   
 */
package com.ontheedgesc.portal;

import java.io.BufferedReader;
import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.ontheedgesc.kubernetesapi.KubernetesApi;
import com.ontheedgesc.portal.util.ResourceLoader;


/**
 * Spring Boot Application
 * 
 * @author markreha
 *
 */
@ComponentScan({ "com.ontheedgesc" })
@SpringBootApplication
public class PortalApplication
{
	@Value( "${app.kubernetesapi.config}" )
	private String apiConfig;
	@Autowired
	KubernetesApi api;

	/**
	 * Main application entry point.
	 * 
	 * @param args Program arguments
	 */
	public static void main(String[] args)
	{
		// Start this Application
		SpringApplication.run(PortalApplication.class, args);
	}

	/**
	 * Post Application Contruction.
	 * 
	 */
	
	@PostConstruct
	public void init() 
	{
		// Initialize the Kubernetes API
		try
		{	
			BufferedReader reader = ResourceLoader.getAsReader(apiConfig);
			try(reader)
			{
				api.initialize(reader);				
			}
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
