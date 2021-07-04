/********************************************************
 *  
 *   Copyright 2021 On The Edge Software Consulting LLC.
 *   All rights reserved.
 *   
 */
package com.ontheedgesc.kubernetesapi;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import io.fabric8.kubernetes.api.model.EnvVar;
import io.fabric8.kubernetes.api.model.EnvVarBuilder;
import io.fabric8.kubernetes.api.model.NodeList;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodList;
import io.fabric8.kubernetes.api.model.Quantity;
import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.ServiceList;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentBuilder;
import io.fabric8.kubernetes.api.model.apps.DeploymentList;
import io.fabric8.kubernetes.api.model.extensions.HTTPIngressPathBuilder;
import io.fabric8.kubernetes.api.model.extensions.Ingress;
import io.fabric8.kubernetes.api.model.extensions.IngressBuilder;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.ExecWatch;
import lombok.extern.slf4j.Slf4j;


/**
 * Kubernetes API Services
 * 
 * @author markreha
 *
 */
@Slf4j
public class KubernetesApi
{
	private KubernetesClient client;
	private CountDownLatch execLatch = new CountDownLatch(1);
	
	/**
	 * Initialize the underlying connection to the Kubernetes Client API and connect to the Cluster.
	 * 
	 * @param kubeConfigPath Path to the Kube Config File
	 * @throws Exception thrown if anything goes wrong during initialization
	 */
	public void initialize(String kubeConfigPath) throws Exception
	{
	    // Read the Kube Config file contents
	    StringBuilder contentBuilder = new StringBuilder();
       	BufferedReader br = new BufferedReader(new FileReader(kubeConfigPath));
        String sCurrentLine;
        while ((sCurrentLine = br.readLine()) != null) 
            contentBuilder.append(sCurrentLine).append("\n");
        br.close();
 	    
	    // Get an instance of the Kubernetes Client setting its configuration from the Kube File contents
        Config config = Config.fromKubeconfig(contentBuilder.toString());
	    this.client = new DefaultKubernetesClient(config);	 
	}

	/**
	 * Initialize the underlying connection to the Kubernetes Client API and connect to the Cluster.
	 * 
	 * @param br BufferedReader instance to the Kube Config File
	 * @throws Exception thrown if anything goes wrong during initialization
	 */
	public void initialize(BufferedReader br) throws Exception
	{
	    // Read the Kube Config file contents
	    StringBuilder contentBuilder = new StringBuilder();
        String sCurrentLine;
        while ((sCurrentLine = br.readLine()) != null) 
            contentBuilder.append(sCurrentLine).append("\n");
        br.close();
 
	    // Get an instance of the Kubernetes Client setting its configuration from the Kube File contents
        Config config = Config.fromKubeconfig(contentBuilder.toString());
	    this.client = new DefaultKubernetesClient(config);	 
	}

	/**
	 * Get the Master Node public IP Address.
	 * 
	 * @return Master Node public IP Address
	 * @throws MalformedURLException thrown if creation of URL string fails
	 */
	public String getMasterIpAddress() throws MalformedURLException
	{
		URL url = new URL(client.getConfiguration().getMasterUrl());
		return url.getHost();
	}

	/**
	 * Get a list of all Pods from desired or all Name Spaces.
	 * 
	 * @param nameSpace Name Space to search, if null returns from all Name Spaces
	 * @return a PodList
	 */
	public PodList getAllPods(String nameSpace)
	{
	    // Call the underlying client API to all the Pods from all Names Spaces
		if(nameSpace == null)
			return client.pods().inAnyNamespace().list();
		else
			return client.pods().inNamespace(nameSpace).list();
	}
	
	/**
	 * Get a Pods log file.
	 * 
	 * @param pod Pod name to get logs for
	 * @param namespace Name Space that the Pod is in
	 * @return String for Logs
	 */
	public String getPodLog(String pod, String namespace)
	{
		return client.pods().inNamespace(namespace).withName(pod).getLog();
	}

	/**
	 * Get a list of all Nodes.
	 * 
	 * @return a NodeList
	 */
	public NodeList getAllNodes()
	{
	    // Call the underlying client API to all the Nodes
	    return client.nodes().list();
	}

	/**
	 * Get a list of all Deployments.
	 * 
	 * @param nameSpace Name Space to search, if null returns from all Name Spaces
	 * @return a DeploymentList
	 */
	public DeploymentList getAllDeployments(String nameSpace)
	{
	    // Call the underlying client API to all the Deployments
		return client.apps().deployments().inNamespace(nameSpace).list();
	}

	/**
	 * Get a Deployment.
	 * 
	 * @param deploymentName Deployment Name Space to search
	 * @param nameSpace Name Space to search
	 * @return a Deployment
	 */
	public Deployment getDeployment(String deploymentName, String nameSpace)
	{
	    // Call the underlying client API to the Deployment
		return client.apps().deployments().inNamespace(nameSpace).withName(deploymentName).get();
	}

	/**
	 * Get a list of all Services from desired or all Name spaces.
	 * 
	 * @param nameSpace Name Space to search, if null returns from all Name Spaces
	 * @return a ServicesList
	 */
	public ServiceList getAllServices(String nameSpace)
	{
	    // Call the underlying client API to all the Nodes
		if(nameSpace == null)
			return client.services().inAnyNamespace().list();
		else
			return client.services().inNamespace(nameSpace).list();
	}
	
	/**
	 * Return the NodePort for the Reverse Proxy.
	 * 
	 * @return NodePort for the Reverse Proxy
	 */
	public String getProxyPort()
	{
		return client.services().inNamespace("kube-system").withName("traefik-ingress-service").get().getSpec().getPorts().get(0).getNodePort().toString();
	}
	
	/**
	 * Create a Service using the specified info.
	 *  
	 *  NOTE on Limits and Request: 
	 *  	Requests are what the container is guaranteed to get. If a container Requests 
	 *  	a resource, Kubernetes will only schedule it on a node that can give it that resource.
	 *  	Limits, make sure a container never goes above a certain value. The container 
	 *  	is only allowed to go up to the limit, and then it is restricted.
	 *  	CPU is specified in millicores and Memory is specified in Bytes.
	 *  
	 * 
	 * @param info Service Specification.
	 * @return Service created.
	 */
	public Service createService(ServiceInfo info)
	{
		// Create list of Environment Variables
		List<EnvVar> envs = new ArrayList<EnvVar>();
		for (Map.Entry<String, String> element : info.getEnvs().entrySet())
		{
			envs.add(new EnvVarBuilder().withName(element.getKey()).withValue(element.getValue()).build());
		}
		
		// Create deployment
		Deployment deployment = new DeploymentBuilder()
				   .withNewMetadata()
				      .withName(info.getName() + "-deployment")
				      .addToLabels(info.getLabels())
				   .endMetadata()
				   .withNewSpec()
				      .withReplicas(info.getReplicas())
				      .withNewTemplate()
				        .withNewMetadata()
				        	.addToLabels("app", info.getName())
				        .endMetadata()
				        .withNewSpec()
				          .addNewContainer()
				             .withName(info.getName())
				             .withImage(info.getImage())
				             .withImagePullPolicy("IfNotPresent")
				             .withEnv(envs)
				             .withNewResources()
				             	.addToLimits(Collections.singletonMap("cpu", new Quantity(info.getCpus() + "m")))
				             	.addToLimits(Collections.singletonMap("memory", new Quantity(info.getMemMBytes() + "Mi")))
				             	.addToRequests(Collections.singletonMap("cpu", new Quantity(info.getCpus() + "m")))
				             	.addToRequests(Collections.singletonMap("memory", new Quantity(info.getMemMBytes() + "Mi")))
				             .endResources()
				             .addNewVolumeMount()
				             	.withName(info.getStorageInfo().getName())				             	
				             	.withMountPath(info.getStorageInfo().getPodPath())
				             	.withSubPath(info.getStorageInfo().getPvSubPath())
				             .endVolumeMount()
				          .endContainer()
				          .addNewVolume()
				          	.withName(info.getStorageInfo().getName())
				          	.withNewPersistentVolumeClaim(info.getStorageInfo().getPvClaimName(), false)
				          .endVolume()
				        .endSpec()
				      .endTemplate()
				      .withNewSelector()
				        .addToMatchLabels("app", info.getName())
				      .endSelector()
				   .endSpec()
				 .build();
		client.apps().deployments().inNamespace(info.getNamespace()).create(deployment);
				
		// Create service
		Service createdSvc = client.services().inNamespace("default").createOrReplaceWithNew()
			    .withNewMetadata()
			    	.withName(info.getName() + "-service")
			    	.addToLabels(info.getLabels())
			    .endMetadata()
			    .withNewSpec()
			    	.addToSelector("app", info.getName())
			    	.withType(info.isUseIngress() ? "ClusterIP" : "NodePort")
			    	.addNewPort()
			    		.withName(info.getTargetPort() + "-" + info.getPublishPort()).withProtocol("TCP").withPort(info.getPublishPort()).withNewTargetPort(info.getTargetPort())
			    	.endPort()
			    .endSpec()
			    .withNewStatus()
			    .endStatus()
			    .done();
		
		// Create Ingress if using Reverse Proxy
		if(info.isUseIngress())
		{
			Ingress ingress = new IngressBuilder() 
					.withNewMetadata()
						.withName(info.getName() + "-ingress")
					    .addToLabels(info.getLabels())
					    .addToAnnotations("kubernetes.io/ingress.class", "traefik")
					.endMetadata()
					.withNewSpec()
						.addNewRule()
							.withNewHttp()
								.withPaths(
											new HTTPIngressPathBuilder()
													.withNewPath("/" + info.getName() + "/")
													.withNewPathType("Prefix")
													.withNewBackend()
														.withServiceName(info.getName() + "-service")
														.withNewServicePort(info.getTargetPort())
													.endBackend()
											.build())				
							.endHttp()
						.endRule()
					.endSpec()
				.build();
			client.extensions().ingresses().inNamespace(info.getNamespace()).create(ingress);
		}
		
		// Create code deployment directory if specified in the Pod (but wait until Pod has been created)
		if(!info.getStorageInfo().getPvSubPath().equals(info.getName()))
		{
			String command = "cd " +  info.getStorageInfo().getPodPath() + " && " + "mkdir " + info.getName() + " && " + "chmod 777 " + info.getName();
			execCommand(info.getName(), "default", command, true);
		}
		else
		{
			String command = "cd " +  info.getStorageInfo().getPodPath() + " && " + "chmod 777 " + ".";
			execCommand(info.getName(), "default", command, true);
		}
		
		// Return the Service created
		return createdSvc;
	}

	/**
	 * Find Services based on matching Labels.
	 * 
	 * @param namespace Name Space to search, if null returns from all Name Spaces
	 * @param labels Labels to search for
	 * @return a ServicesList
	 */
	public ServiceList findServices(String namespace, Map<String, String> labels)
	{
	    // Call the underlying client API to all the Services with the matching Labels
		if(namespace == null)
			return client.services().inAnyNamespace().withLabels(labels).list();
		else
			return client.services().inNamespace(namespace).withLabels(labels).list();
	}
	
	/**
	 * Delete a Deployment using the specified info.
	 * 
	 * @param serviceName Service as a Name to delete
	 * @param namespace Deployment in the namepace to delete
	 * @return True if OK else False if deletion failed
	 */
	public boolean deleteDeployment(String serviceName, String namespace)
	{
		// Find the Deployment mount path
		Deployment deployment = client.apps().deployments().inNamespace(namespace).withName(serviceName + "-deployment").get();
		String mountPath = deployment.getSpec().getTemplate().getSpec().getContainers().get(0).getVolumeMounts().get(0).getMountPath();
		String subPath = deployment.getSpec().getTemplate().getSpec().getContainers().get(0).getVolumeMounts().get(0).getSubPath();

		// Delete code deployment directory in the Pod
		if(!subPath.equals(serviceName))
		{
			String command = "cd " +  mountPath + " && " + "rm -r " + serviceName;
			execCommand(serviceName, namespace, command, false);
		}
		else
		{
			String command = "cd " +  mountPath + " && " + "rm -rf *";
			execCommand(serviceName, namespace, command, false);			
		}

		// Delete Deployment
		client.apps().deployments().inNamespace(namespace).withName(serviceName + "-deployment").delete();
		
		// Delete Service
		Boolean isDeleted = client.services().inNamespace(namespace).withName(serviceName + "-service").delete();
		if(!isDeleted.booleanValue())
			return false;
		
		// All OK
		return true;
	}
	
	/**
	 * Execute a command in a desired Pod
	 * 
	 * @param podName The name of the Pod
	 * @param namespace The namespace for the Pod
	 * @param command Bash command(s) to run, if multiple commands they need to follow Bash conventions and separate with {@literal&& or ||}
	 * @param waitForPod True if need to wait for Pod to be in a running state first
	 * @return Standard Output from Exec Command
	 */
	public String execCommand(String podName, String namespace, String command, boolean waitForPod)
	{
        ByteArrayOutputStream out = new ByteArrayOutputStream();		
		log.info("execCommand() Excec Command is " + command);
		try
		{
			// Wait in case the Pod was just created
			if(waitForPod)
				Thread.sleep(20000);
		
			// Find the Pod and make sure it is ready
			Pod pod = client.pods().inNamespace(namespace).withLabel("app", podName).list().getItems().get(0);
			if(waitForPod)
				client.pods().inNamespace(namespace).withName(pod.getMetadata().getName()).waitUntilReady(10, TimeUnit.SECONDS);
		
			// Execute the desired Command
			ExecWatch execWatch = client.pods().inNamespace("default").withName(pod.getMetadata().getName()).writingOutput(out).writingError(out).exec("sh", "-c", command);
			execLatch.await(5, TimeUnit.SECONDS);
		
			// Cleanup and return results
			execWatch.close();
		}
		catch(Exception e) 
		{
			out.reset();
			out.writeBytes(e.getMessage().getBytes());			
		}
		log.info("execCommand() Excec Command System Out is " + out);
	    return out.toString();
	}
	
	/**
	 * Copy a File to a desired Pod.
	 * 
	 * @param podName Pod to copy to
	 * @param namespace In this namespace
	 * @param fileToUpload File to upload to Pod
	 * @param podPath At this path in the Pod
	 * @return True if OK
	 */
	public boolean copyFileToPod(String podName, String namespace, File fileToUpload, String podPath)
	{
		// Find the Pod then upload specified file to it
		log.info("copyFileToPod() uploading/deploying file " + fileToUpload.toPath());
		Pod pod = client.pods().inNamespace(namespace).withLabel("app", podName).list().getItems().get(0);
		boolean ok = client.pods().inNamespace(namespace).withName(pod.getMetadata().getName()).file(podPath).upload(fileToUpload.toPath());
		
		// See if a TarGz file was copied and if so run Exec Command in the Pod to extract it
		String filename = fileToUpload.toPath().toString();
		if(filename.contains("."))
		{
			// Parse the file that is being copied to see if it is a TarGz file
			String extension = filename.substring(filename.lastIndexOf(".") + 1);
			log.info("Upload File Extension is " + extension);
			if(extension.compareToIgnoreCase("gz") == 0)
			{
				// Find the Deployment mount path
				Deployment deployment = client.apps().deployments().inNamespace(namespace).withName(podName + "-deployment").get();
				String mountPath = deployment.getSpec().getTemplate().getSpec().getContainers().get(0).getVolumeMounts().get(0).getMountPath();
				String completePodPath  = mountPath + "/" + podName;

				// Run Unzip Command in the Pod
				String file = filename.substring(filename.lastIndexOf(File.separator) + 1);
				log.info("Upload File is " + file + " and will be uncompressed");
				String command = "cd " +  completePodPath + " && " + "tar xvzf " + file + " -C ." + " && " + "rm " + file + " && " + "chown root:root * && chmod 777 *";
				execCommand(podName, namespace, command, false);
			}
		}
		
		// Return
		return ok;
	}

	/**
	 * Deploy Code to a desired Pod.
	 * 
	 * @param podName Pod to deploy code to
	 * @param namespace In this namespace
	 * @param fileToUpload File to upload to Pod
	 * @return True if OK
	 */
	public boolean deployCodeToPod(String podName, String namespace, File fileToUpload)
	{
		// Determine where the file needs to be copied to
		Deployment deployment = getDeployment(podName + "-deployment", namespace);
		String mountPath = deployment.getSpec().getTemplate().getSpec().getContainers().get(0).getVolumeMounts().get(0).getMountPath();
		String completePodPath = mountPath + "/" + podName;
		String podPath = mountPath + "/" + podName + "/" + fileToUpload.getName();
		
		// Delete any existing code
		String command = "cd " +  completePodPath + " && " + "rm -r *";
		execCommand(podName, namespace, command, false);
		
		// Copy the Deply TarGz to the Pod (this will also uncompress it)
		return copyFileToPod(podName, namespace, fileToUpload, podPath);
	}
	
	/**
	 * Cleanup the connections etc.
	 * 
	 */	
	public void cleanup()
	{
		// Call underlying client API to close things
	    client.close();
	}
}
