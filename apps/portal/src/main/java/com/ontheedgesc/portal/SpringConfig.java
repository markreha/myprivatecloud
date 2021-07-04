/********************************************************
 *  
 *   Copyright 2021 On The Edge Software Consulting LLC.
 *   All rights reserved.
 *   
 */
package com.ontheedgesc.portal;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ontheedgesc.kubernetesapi.KubernetesApi;


/**
 * Spring Boot Application Configuration
 * 
 * @author markreha
 *
 */
@Configuration
public class SpringConfig
{
	/**
	 * Managed Kubernetes API Bean.
	 * 
	 * @return Instance of Kubernetes API Bean
	 */
	@Bean(name="kubernetesApi")
	public KubernetesApi getgetKubernetesApi()
	{
		return new KubernetesApi();
	}
}
