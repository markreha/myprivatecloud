package com.ontheedgesc.test;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ontheedgesc.kubernetesapi.KubernetesApi;
import com.ontheedgesc.kubernetesapi.ServiceInfo;
import com.ontheedgesc.kubernetesapi.StorageInfo;

import io.fabric8.kubernetes.api.model.Node;
import io.fabric8.kubernetes.api.model.NodeList;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodList;
import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.ServiceList;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentList;

/**
 * Test Class for the Kubernetes Service API's.
 * 
 * @author markreha
 *
 */

public class Play
{
	// Kubernetes Configuration Files (get this from the Master Node at $HOME/.kube/config)
	static String CONFIG = "./config-cluster";								// Active Kubernetes Config

	// Local variables
	static Scanner reader = new Scanner(System.in);							// Input Scanner used for the Menu
	static KubernetesApi api = new KubernetesApi();							// Kubernetes Client API
	static boolean displayDefaultNamespace = true;							// Set to true to display only the Default Namespace
	static String masterNodeIpAddress = "";									// Master IP Address for display
    static Logger logger = LoggerFactory.getLogger(Play.class);				// Logger for this class
	
	// Storage Claim Options (these need to match PVC's deployed on the Master Node
	static String LOCAL_PV_CLAIM = "local-small-pv-claim";	
	static String NETWORK_PV_CLAIM = "network-small-pv-claim";	


	/**
	 * Test Kubernetes Java API using the Fabric8io Kubernetes Java Client API
	 * See documentation at https://github.com/fabric8io/kubernetes-client
	 * 
	 * @param args
	 * @throws IOException
	 * @throws ApiException
	 */
	public static void main(String[] args)
	{
	    // Set the active Kube Config file and initialize Kubernetes Client
	    String kubeConfigPath = Play.CONFIG;
	    
		// Main menu
		int cmd = -1;
		try 
		{
			// Initialize connecting to Kubernetes Cluster
			System.out.println("\nMy Private Cloud Kubernetes API Test Application v1.0.0");
			logger.debug("=======> Intitializing.........");		
			api.initialize(kubeConfigPath);
			masterNodeIpAddress = "http://" + api.getMasterIpAddress();
			logger.info("Connected to the Kubernetes Master Node at " + masterNodeIpAddress + "/");

			// Main Testing Loop
			do
			{
				// Display the Menu
				System.out.println("");
				System.out.println("");
				System.out.println("=====================================================");
				System.out.println("===== My Private Cloud Kubernetes API Test Menu =====");
				System.out.println("=====================================================");
				System.out.println("");
				System.out.println("      0 to Quit");
				System.out.println("      1 to List Nodes");
				System.out.println("      2 to List Pods");
				System.out.println("      3 to List Services");
				System.out.println("      4 to List Deployments");
				System.out.println("      5 to Create a Nginx Service");
				System.out.println("      6 to Create a Tomcat 8.5 Service");
				System.out.println("      7 to Create a Tomcat 9.0 Service");
				System.out.println("      8 to Create a PHP Service");
				System.out.println("      9 to Create a MySQL Service");
				System.out.println("     10 to Create a PostgreSQL Service");
				System.out.println("     11 to Create a MongoDB Service");
				System.out.println("     12 to Create a TBD Service");
				System.out.println("     13 to Delete a Deployment");
				System.out.println("     14 to Get a Pods Log file");
				System.out.println("     15 to Search for Services");
				System.out.println("     16 to Deploy Code to a Deployment");
				System.out.println("     17 to Set if Default Namespace is On/Off");
				System.out.print("  Enter a Command: ");
			
				// Get Menu Option from the user and run the specified command
				cmd = reader.nextInt();
				switch (cmd)
				{
					case 1:
						listAllNodes();
						break;
					case 2:
						listAllPods();
						break;
					case 3:
						listAllServices();
						break;
					case 4:
						listAllDeployments();
						break;
					case 5:
						createNginxService();
						break;
					case 6:
						createTomcatService("tomcat", "Tomcat v8.5 on Java 8 Server", "tomcat:8.5-alpine");
						break;
					case 7:
						createTomcatService("tomcat", "Tomcat v9.0 on Java 11 Server", "tomcat:9.0.48-jdk11-adoptopenjdk-hotspot");
						break;
					case 8:
						createPhpService();
						break;
					case 9:
						createMariaDBService();
						break;
					case 10:
						createPostgreSQLService();
						break;
					case 11:
						createMongoDBService();
						break;
					case 12:
						createTbdService();
						break;
					case 13:
						deleteDeployment();
						break;
					case 14:
						getPodLogs();
						break;
					case 15:
						findServices();
						break;
					case 16:
						deployCodeToDeployment();
						break;
					case 17:
						toggleDefaultName();
						break;
				}
			}while (cmd != 0);
		}
		catch (NoSuchElementException e)
		{			
		}
		catch (Exception e)
		{
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			logger.info("Exception Stack Trace is" + sw.toString());
		}
		finally
		{		
			// Cleanup
			api.cleanup();
			reader.close();
			System.out.println("Bye");
		}
	}
	
	// ***** Private Helper Functions *****
	
	private static boolean listAllPods()
	{
		// Call the Client API to get a list of all Pods
		PodList podList = api.getAllPods(displayDefaultNamespace ? "default" : null);
		if(podList.getItems().size() == 0)
		{
			System.out.println("=======> No Pods available");
			return false;
		}
	    for (Pod item : podList.getItems()) 
	    {
	    	System.out.println("=======> " + " Pod Name of " + item.getMetadata().getName() 
	    									+ " in Name Space " + item.getMetadata().getNamespace()
	    									+ " in Node " + item.getSpec().getNodeName()
	    									+ " with a status of " + item.getStatus().getPhase());
	    }
	    return true;
	}

	private static boolean getPodLogs()
	{
		// Display all Pods and prompt the user for which POd from Name Space to get logs for
		if(!listAllPods())
			return false;
		System.out.print("Enter Pod to get: ");
		String pod = reader.next();
		System.out.print("From Namespace: ");
		String namespace = reader.next();
		
		// Call the Client API to get a Pods log files
		String logs = api.getPodLog(pod, namespace);
		System.out.println("=======> Pod logs: \n" + logs);
		return true;
	}

	private static boolean listAllNodes()
	{
		// Call the Client API to get a list of all Nodes
		NodeList nodeList = api.getAllNodes();
		if(nodeList.getItems().size() == 0)
		{
			System.out.println("=======> No Nodes available");
			return false;
		}
	    for (Node item : nodeList.getItems()) 
	    {
	    	System.out.println("=======> " + " Node Name of " + item.getMetadata().getName());
	    }
	    return true;
	}	

	private static boolean listAllDeployments()
	{
		// Call the Client API to get a list of all Deployments
		DeploymentList deploymentList = api.getAllDeployments(displayDefaultNamespace ? "default" : null);
		if(deploymentList.getItems().size() == 0)
		{
			System.out.println("=======> No Deployments available");
			return false;
		}
	    for (Deployment item : deploymentList.getItems()) 
	    {
	    	System.out.println("=======> " + " Deployment Name of " + item.getMetadata().getName() 
	    									+ " in Name Space " + item.getMetadata().getNamespace() 
	    									+ " with a available replicas of " + item.getStatus().getAvailableReplicas());
	    }
	    return true;
	}

	private static boolean listAllServices()
	{
		// Call the Client API to get a list of all Services
		ServiceList serviceList = api.getAllServices(displayDefaultNamespace ? "default" : null);
		if(serviceList.getItems().size() == 0)
		{
			System.out.println("=======> No Services available");
			return false;
		}
	    for (Service item : serviceList.getItems()) 
	    {
	    	System.out.println("=======> " + " Service Name of " + item.getMetadata().getName() 
	    										+ " in Name Space " + item.getMetadata().getNamespace()
	    										+ " with Type of " + item.getSpec().getType()
	    										+ " with a Cluster IP of " + item.getSpec().getClusterIP() 
	    										+ " with an External IP of " + ((item.getSpec().getExternalIPs().size() == 0) ? "<none>" : item.getSpec().getExternalIPs().get(0))
	    										+ " with Port Spec of " + item.getSpec().getPorts().get(0).getPort() + (item.getSpec().getPorts().get(0).getNodePort() == null ? "" : ":" + item.getSpec().getPorts().get(0).getNodePort())  + "/" + item.getSpec().getPorts().get(0).getProtocol());
	    }
	    return true;
	}
	
	private static boolean findServices()
	{
		// Prompt the user for which Service based on Label they want to find
		System.out.print("Enter Namespace to search: ");
		String namespace = reader.next();
		System.out.print("Enter Label Name to search: ");
		String name = reader.next();
		System.out.print("Enter Label Value to search: ");
		String value = reader.next();

		// Call the Client API to search for Service
		HashMap<String, String> labels = new HashMap<String, String>();
		labels.put(name, value);
		ServiceList serviceList = api.findServices(namespace, labels);
		if(serviceList.getItems().size() == 0)
		{
			System.out.println("=======> No Services available");
			return false;
		}
	    for (Service item : serviceList.getItems()) 
	    {
	    	System.out.println("=======> " + " Service Name of " + item.getMetadata().getName() 
											+ " in Name Space " + item.getMetadata().getNamespace() 
    										+ " with Type of " + item.getSpec().getType()
											+ " with a Cluster IP of " + item.getSpec().getClusterIP() 
											+ " with an External IP of " + ((item.getSpec().getExternalIPs().size() == 0) ? "<none>" : item.getSpec().getExternalIPs().get(0))
											+ " with Port Spec of " + item.getSpec().getPorts().get(0).getPort() + (item.getSpec().getPorts().get(0).getNodePort() == null ? "" : ":" + item.getSpec().getPorts().get(0).getNodePort())  + "/" + item.getSpec().getPorts().get(0).getProtocol());
	    }
	    return true;
	}
	
	private static boolean createNginxService()
	{
		// Set the Service Info (this would normally come from the database)
		//  Service Info
		String userId = "0";
		String longName = "Nginx v1.2 Web Server";
		String shortName = "nginx".toLowerCase();
		String dockerImageName = "nginx";
		boolean canDeployCodeTo = true;
		String serviceName = shortName + userId + "-" + System.currentTimeMillis();
		String namespace = "default";
		HashMap<String, String> labels = new HashMap<String, String>();
			labels.put("app", shortName);
			labels.put("environment", "pi");
			labels.put("owner", userId);
		HashMap<String, String> envs = new HashMap<String, String>();
		float cpus = .5f;
		int memMBytes = 500;
		int replicas = 1;
		int targetPort = 80;
		int publishPort = 80;
		boolean useProxy = canDeployCodeTo;
		//  Storage Info
		String podPath = "/usr/share/nginx/html";
		String pvSubPath = serviceName;
		String pvClaim = NETWORK_PV_CLAIM; 				
				
		// Initialize all the Service Info Object Models
		StorageInfo storage = new StorageInfo(shortName + "-pv", podPath, pvSubPath, pvClaim);
		ServiceInfo info = new ServiceInfo(dockerImageName, serviceName, namespace, labels, envs, cpus, memMBytes, replicas, targetPort, publishPort, storage, useProxy);
		
		// Call the Client API to create the Service
		System.out.println("=======> Creating " + longName + " Service......");
		Service service = api.createService(info);
    	System.out.println("=======> " + " Service Name of " + service.getMetadata().getName() 
										+ " in Name Space " + service.getMetadata().getNamespace() 
										+ " with Type of " + service.getSpec().getType()
										+ " with a Cluster IP of " + service.getSpec().getClusterIP() 
										+ " with an External IP of " + ((service.getSpec().getExternalIPs().size() == 0) ? "<none>" : service.getSpec().getExternalIPs().get(0))
										+ " with Port Spec of " + service.getSpec().getPorts().get(0).getPort() + (service.getSpec().getPorts().get(0).getNodePort() == null ? "" : ":" + service.getSpec().getPorts().get(0).getNodePort())  + "/" + service.getSpec().getPorts().get(0).getProtocol());

    	// Copy the default Web Page to the Service Pod
    	if(canDeployCodeTo)
    	{
    		URL resource = Play.class.getClassLoader().getResource("resources/defaults/index.html");
    		if (resource != null)
    		{
    			try 
    			{
    				File htmlFile = new File(resource.toURI());
    				api.copyFileToPod(serviceName, "default", htmlFile, podPath + "/" + serviceName + "/" + "index.html");
    			} 
    			catch (URISyntaxException e) 
    			{
    				logger.info("Deploying default Web Page failed");
    				e.printStackTrace();
    			}
    		}
    		else
    		{
    			logger.info("Could not load index.html file as a resource");
    		}
    	}

    	// Display all the useful information the User needs to know
    	if(useProxy)
    		System.out.println("=======>  Access your application at " + masterNodeIpAddress + ":" + api.getProxyPort() + "/" + serviceName + "/");
    	else
    		System.out.println("=======>  Access your application at " + masterNodeIpAddress + ":" + service.getSpec().getPorts().get(0).getNodePort() + "/" + serviceName + "/");   		
    	if(pvClaim.equals(LOCAL_PV_CLAIM))
    	{
    		System.out.println("=======>  Find your Node by listing all the Pods and searching for one that starts with: " + serviceName + "-deployment");
    		if(canDeployCodeTo)
    			System.out.println("=======>  Your code will be deployed to " + "/cloudsapps/local/" + pvSubPath + "/" + serviceName);
    		else
    			System.out.println("=======>  Your application is located at " + "/cloudsapps/local/" + pvSubPath);
    	}
    	else
    	{
    		if(canDeployCodeTo)
    			System.out.println("=======>  Your code will be deployed to " + "smb//10.0.0.133/share/" + pvSubPath + "/" + serviceName);
    		else
    			System.out.println("=======>  Your application is located at " + "smb//10.0.0.133/share/" + pvSubPath);
    	}  
 
    	// Return all OK
    	return true;
	}

	private static boolean createTomcatService(String name, String description, String dockerImage)
	{
		// Setup all the Service Info data (this would normally come from the database)
		//  Service Info
		String userId = "0";
		String longName = description;
		String shortName = name.toLowerCase();
		String dockerImageName = dockerImage;
		boolean canDeployCodeTo = true;
		String serviceName = shortName + userId + "-" + System.currentTimeMillis();
		String namespace = "default";
		HashMap<String, String> labels = new HashMap<String, String>();
			labels.put("app", shortName);
			labels.put("environment", "pi");
			labels.put("owner", userId);
		HashMap<String, String> envs = new HashMap<String, String>();
		float cpus = .5f;
		int memMBytes = 500;
		int replicas = 1;
		int targetPort = 8080;
		int publishPort = 8080;
		boolean useProxy = canDeployCodeTo;
		//  Storage Info
		String podPath = "/usr/local/tomcat/webapps";
		String pvSubPath = serviceName;
		String pvClaim = NETWORK_PV_CLAIM;
		
		// Initialize all the Service Info Object Models
		StorageInfo storage = new StorageInfo(shortName + "-pv", podPath, pvSubPath, pvClaim);
		ServiceInfo info = new ServiceInfo(dockerImageName, serviceName, namespace, labels, envs, cpus, memMBytes, replicas, targetPort, publishPort, storage, useProxy);
		
		// Call the Client API to create the Service
		System.out.println("=======> Creating " + longName + " Service......");
		Service service = api.createService(info);
    	System.out.println("=======> " + " Service Name of " + service.getMetadata().getName() 
										+ " in Name Space " + service.getMetadata().getNamespace() 
										+ " with a Cluster IP of " + service.getSpec().getClusterIP() 
										+ " with Type of " + service.getSpec().getType()
										+ " with an External IP of " + ((service.getSpec().getExternalIPs().size() == 0) ? "<none>" : service.getSpec().getExternalIPs().get(0))
										+ " with Port Spec of " + service.getSpec().getPorts().get(0).getPort() + (service.getSpec().getPorts().get(0).getNodePort() == null ? "" : ":" + service.getSpec().getPorts().get(0).getNodePort())  + "/" + service.getSpec().getPorts().get(0).getProtocol());

    	// Copy the default Web Page to the Service Pod
    	if(canDeployCodeTo)
    	{
    		URL resource = Play.class.getClassLoader().getResource("resources/defaults/index.html");
    		if (resource != null)
    		{
    			try 
    			{
    				File htmlFile = new File(resource.toURI());
    				api.copyFileToPod(serviceName, "default", htmlFile, podPath + "/" + serviceName + "/" + "index.html");
    			} 
    			catch (URISyntaxException e) 
    			{
    				logger.info("Deploying default Web Page failed");
    				e.printStackTrace();
    			}
    		}
    		else
    		{
    			logger.info("Could not load index.html file as a resource");
    		}
    	}

    	// Display all the useful information the User needs to know
    	if(useProxy)
    		System.out.println("=======>  Access your application at " + masterNodeIpAddress + ":" + api.getProxyPort() + "/" + serviceName + "/");
    	else
    		System.out.println("=======>  Access your application at " + masterNodeIpAddress + ":" + service.getSpec().getPorts().get(0).getNodePort() + "/" + serviceName + "/");   		
    	if(pvClaim.equals(LOCAL_PV_CLAIM))
    	{
    		System.out.println("=======>  Find your Node by listing all the Pods and searching for one that starts with: " + serviceName + "-deployment");
    		if(canDeployCodeTo)
    			System.out.println("=======>  Your code will be deployed to " + "/cloudsapps/local/" + pvSubPath + "/" + serviceName);
    		else
    			System.out.println("=======>  Your application is located at " + "/cloudsapps/local/" + pvSubPath);
    	}
    	else
    	{
    		if(canDeployCodeTo)
    			System.out.println("=======>  Your code will be deployed to " + "smb//10.0.0.133/share/" + pvSubPath + "/" + serviceName);
    		else
    			System.out.println("=======>  Your application is located at " + "smb//10.0.0.133/share/" + pvSubPath);
    	}  
    	
    	// Return all OK
    	return true;
	}

	private static boolean createPhpService()
	{
		// Setup all the Service Info data (this would normally come from the database)
		//  Service Info
		String userId = "0";
		String longName = "PHP 7.4 Server";
		String shortName = "php".toLowerCase();
		String dockerImageName = "php:7.4-apache";
		boolean canDeployCodeTo = true;
		String serviceName = shortName + userId + "-" + System.currentTimeMillis();
		String namespace = "default";
		HashMap<String, String> labels = new HashMap<String, String>();
			labels.put("app", shortName);
			labels.put("environment", "pi");
			labels.put("owner", userId);
		HashMap<String, String> envs = new HashMap<String, String>();
		float cpus = .5f;
		int memMBytes = 500;
		int replicas = 1;
		int targetPort = 80;
		int publishPort = 80;
		boolean useProxy = canDeployCodeTo;
		//  Storage Info
		String podPath = "/var/www/html";
		String pvSubPath = serviceName;
		String pvClaim = NETWORK_PV_CLAIM;
		
		// Initialize all the Service Info Object Models
		StorageInfo storage = new StorageInfo(shortName + "-pv", podPath, pvSubPath, pvClaim);
		ServiceInfo info = new ServiceInfo(dockerImageName, serviceName, namespace, labels, envs, cpus, memMBytes, replicas, targetPort, publishPort, storage, useProxy);
		
		// Call the Client API to create the Service
		System.out.println("=======> Creating " + longName + " Service......");
		Service service = api.createService(info);
    	System.out.println("=======> " + " Service Name of " + service.getMetadata().getName() 
										+ " in Name Space " + service.getMetadata().getNamespace() 
										+ " with a Cluster IP of " + service.getSpec().getClusterIP() 
										+ " with Type of " + service.getSpec().getType()
										+ " with an External IP of " + ((service.getSpec().getExternalIPs().size() == 0) ? "<none>" : service.getSpec().getExternalIPs().get(0))
										+ " with Port Spec of " + service.getSpec().getPorts().get(0).getPort() + (service.getSpec().getPorts().get(0).getNodePort() == null ? "" : ":" + service.getSpec().getPorts().get(0).getNodePort())  + "/" + service.getSpec().getPorts().get(0).getProtocol());

    	// Copy the default Web Page to the Service Pod
    	if(canDeployCodeTo)
    	{
    		URL resource = Play.class.getClassLoader().getResource("resources/defaults/index.html");
    		if (resource != null)
    		{
    			try 
    			{
    				File htmlFile = new File(resource.toURI());
    				api.copyFileToPod(serviceName, "default", htmlFile, podPath + "/" + serviceName + "/" + "index.html");
    			} 
    			catch (URISyntaxException e) 
    			{
    				logger.info("Deploying default Web Page failed");
    				e.printStackTrace();
    			}
    		}
    		else
    		{
    			logger.info("Could not load index.html file as a resource");
    		}
    	}

    	// Display all the useful information the User needs to know
    	if(useProxy)
    		System.out.println("=======>  Access your application at " + masterNodeIpAddress + ":" + api.getProxyPort() + "/" + serviceName + "/");
    	else
    		System.out.println("=======>  Access your application at " + masterNodeIpAddress + ":" + service.getSpec().getPorts().get(0).getNodePort() + "/" + serviceName + "/");   		
    	if(pvClaim.equals(LOCAL_PV_CLAIM))
    	{
    		System.out.println("=======>  Find your Node by listing all the Pods and searching for one that starts with: " + serviceName + "-deployment");
    		if(canDeployCodeTo)
    			System.out.println("=======>  Your code will be deployed to " + "/cloudsapps/local/" + pvSubPath + "/" + serviceName);
    		else
    			System.out.println("=======>  Your application is located at " + "/cloudsapps/local/" + pvSubPath);
    	}
    	else
    	{
    		if(canDeployCodeTo)
    			System.out.println("=======>  Your code will be deployed to " + "smb//10.0.0.133/share/" + pvSubPath + "/" + serviceName);
    		else
    			System.out.println("=======>  Your application is located at " + "smb//10.0.0.133/share/" + pvSubPath);
    	}  
    	
    	// Return all OK
    	return true;
	}

	private static boolean createMariaDBService()
	{
		// Set the Service Info (this would normally come from the database)
		//  Service Info
		String userId = "0";
		String longName = "MariaDB (MySQL) Database";
		String shortName = "mariadb".toLowerCase();
		String dockerImageName = "ghcr.io/linuxserver/mariadb";
		boolean canDeployCodeTo = false;
		String serviceName = shortName + userId + "-" + System.currentTimeMillis();
		String namespace = "default";
		HashMap<String, String> labels = new HashMap<String, String>();
			labels.put("app", shortName);
			labels.put("environment", "pi");
			labels.put("owner", userId);
		HashMap<String, String> envs = new HashMap<String, String>();
			envs.put("MYSQL_ROOT_PASSWORD", "mypassword");
		float cpus = .5f;
		int memMBytes = 500;
		int replicas = 1;
		int targetPort = 3306;
		int publishPort = 3306;
		boolean useProxy = canDeployCodeTo;
		//  Storage Info
		String podPath = "/config/databases";
		String pvSubPath = serviceName;
		String pvClaim = LOCAL_PV_CLAIM;	// TODO: Fix why MariaDB fails to work on FAT32 Network drive 				
				
		// Initialize all the Service Info Object Models
		StorageInfo storage = new StorageInfo(shortName + "-pv", podPath, pvSubPath, pvClaim);
		ServiceInfo info = new ServiceInfo(dockerImageName, serviceName, namespace, labels, envs, cpus, memMBytes, replicas, targetPort, publishPort, storage, useProxy);
		
		// Call the Client API to create the Service
		System.out.println("=======> Creating " + longName + " Service......");
		Service service = api.createService(info);
    	System.out.println("=======> " + " Service Name of " + service.getMetadata().getName() 
										+ " in Name Space " + service.getMetadata().getNamespace() 
										+ " with Type of " + service.getSpec().getType()
										+ " with a Cluster IP of " + service.getSpec().getClusterIP() 
										+ " with an External IP of " + ((service.getSpec().getExternalIPs().size() == 0) ? "<none>" : service.getSpec().getExternalIPs().get(0))
										+ " with Port Spec of " + service.getSpec().getPorts().get(0).getPort() + (service.getSpec().getPorts().get(0).getNodePort() == null ? "" : ":" + service.getSpec().getPorts().get(0).getNodePort())  + "/" + service.getSpec().getPorts().get(0).getProtocol());

    	// Copy the default Web Page to the Service Pod
    	if(canDeployCodeTo)
    	{
    		URL resource = Play.class.getClassLoader().getResource("resources/defaults/index.html");
    		if (resource != null)
    		{
    			try 
    			{
    				File htmlFile = new File(resource.toURI());
    				api.copyFileToPod(serviceName, "default", htmlFile, podPath + "/" + serviceName + "/" + "index.html");
    			} 
    			catch (URISyntaxException e) 
    			{
    				logger.info("Deploying default Web Page failed");
    				e.printStackTrace();
    			}
    		}
    		else
    		{
    			logger.info("Could not load index.html file as a resource");
    		}
    	}

    	// Display all the useful information the User needs to know
    	if(useProxy)
    		System.out.println("=======>  Access your application at " + masterNodeIpAddress + ":" + api.getProxyPort() + "/" + serviceName + "/");
    	else
    		System.out.println("=======>  Access your application at " + masterNodeIpAddress + ":" + service.getSpec().getPorts().get(0).getNodePort() + "/" + serviceName + "/");   		
    	if(pvClaim.equals(LOCAL_PV_CLAIM))
    	{
    		System.out.println("=======>  Find your Node by listing all the Pods and searching for one that starts with: " + serviceName + "-deployment");
    		if(canDeployCodeTo)
    			System.out.println("=======>  Your code will be deployed to " + "/cloudsapps/local/" + pvSubPath + "/" + serviceName);
    		else
    			System.out.println("=======>  Your application is located at " + "/cloudsapps/local/" + pvSubPath);
    	}
    	else
    	{
    		if(canDeployCodeTo)
    			System.out.println("=======>  Your code will be deployed to " + "smb//10.0.0.133/share/" + pvSubPath + "/" + serviceName);
    		else
    			System.out.println("=======>  Your application is located at " + "smb//10.0.0.133/share/" + pvSubPath);
    	}  
 
    	// Return all OK
    	return true;
	}

	private static boolean createPostgreSQLService()
	{
		// Set the Service Info (this would normally come from the database)
		//  Service Info
		String userId = "0";
		String longName = "PostgreSQL 13.3 Database";
		String shortName = "postgres".toLowerCase();
		String dockerImageName = "postgres";
		boolean canDeployCodeTo = false;
		String serviceName = shortName + userId + "-" + System.currentTimeMillis();
		String namespace = "default";
		HashMap<String, String> labels = new HashMap<String, String>();
			labels.put("app", shortName);
			labels.put("environment", "pi");
			labels.put("owner", userId);
		HashMap<String, String> envs = new HashMap<String, String>();
			envs.put("POSTGRES_PASSWORD", "mypassword");
		float cpus = .5f;
		int memMBytes = 500;
		int replicas = 1;
		int targetPort = 5432;
		int publishPort = 5432;
		boolean useProxy = canDeployCodeTo;
		//  Storage Info
		String podPath = "/var/lib/postgresql/data";
		String pvSubPath = serviceName;
		String pvClaim = LOCAL_PV_CLAIM;				
				
		// Initialize all the Service Info Object Models
		StorageInfo storage = new StorageInfo(shortName + "-pv", podPath, pvSubPath, pvClaim);
		ServiceInfo info = new ServiceInfo(dockerImageName, serviceName, namespace, labels, envs, cpus, memMBytes, replicas, targetPort, publishPort, storage, useProxy);
		
		// Call the Client API to create the Service
		System.out.println("=======> Creating " + longName + " Service......");
		Service service = api.createService(info);
    	System.out.println("=======> " + " Service Name of " + service.getMetadata().getName() 
										+ " in Name Space " + service.getMetadata().getNamespace() 
										+ " with Type of " + service.getSpec().getType()
										+ " with a Cluster IP of " + service.getSpec().getClusterIP() 
										+ " with an External IP of " + ((service.getSpec().getExternalIPs().size() == 0) ? "<none>" : service.getSpec().getExternalIPs().get(0))
										+ " with Port Spec of " + service.getSpec().getPorts().get(0).getPort() + (service.getSpec().getPorts().get(0).getNodePort() == null ? "" : ":" + service.getSpec().getPorts().get(0).getNodePort())  + "/" + service.getSpec().getPorts().get(0).getProtocol());

    	// Copy the default Web Page to the Service Pod
    	if(canDeployCodeTo)
    	{
    		URL resource = Play.class.getClassLoader().getResource("resources/defaults/index.html");
    		if (resource != null)
    		{
    			try 
    			{
    				File htmlFile = new File(resource.toURI());
    				api.copyFileToPod(serviceName, "default", htmlFile, podPath + "/" + serviceName + "/" + "index.html");
    			} 
    			catch (URISyntaxException e) 
    			{
    				logger.info("Deploying default Web Page failed");
    				e.printStackTrace();
    			}
    		}
    		else
    		{
    			logger.info("Could not load index.html file as a resource");
    		}
    	}

    	// Display all the useful information the User needs to know
    	if(useProxy)
    		System.out.println("=======>  Access your application at " + masterNodeIpAddress + ":" + api.getProxyPort() + "/" + serviceName + "/");
    	else
    		System.out.println("=======>  Access your application at " + masterNodeIpAddress + ":" + service.getSpec().getPorts().get(0).getNodePort() + "/" + serviceName + "/");   		
    	if(pvClaim.equals(LOCAL_PV_CLAIM))
    	{
    		System.out.println("=======>  Find your Node by listing all the Pods and searching for one that starts with: " + serviceName + "-deployment");
    		if(canDeployCodeTo)
    			System.out.println("=======>  Your code will be deployed to " + "/cloudsapps/local/" + pvSubPath + "/" + serviceName);
    		else
    			System.out.println("=======>  Your application is located at " + "/cloudsapps/local/" + pvSubPath);
    	}
    	else
    	{
    		if(canDeployCodeTo)
    			System.out.println("=======>  Your code will be deployed to " + "smb//10.0.0.133/share/" + pvSubPath + "/" + serviceName);
    		else
    			System.out.println("=======>  Your application is located at " + "smb//10.0.0.133/share/" + pvSubPath);
    	}  
 
    	// Return all OK
    	return true;
	}

	private static boolean createMongoDBService()
	{
		// Set the Service Info (this would normally come from the database)
		//  Service Info
		String userId = "0";
		String longName = "MonoDB Database";
		String shortName = "mongodb".toLowerCase();
		String dockerImageName = "webhippie/mongodb";
		boolean canDeployCodeTo = false;
		String serviceName = shortName + userId + "-" + System.currentTimeMillis();
		String namespace = "default";
		HashMap<String, String> labels = new HashMap<String, String>();
			labels.put("app", shortName);
			labels.put("environment", "pi");
			labels.put("owner", userId);
		HashMap<String, String> envs = new HashMap<String, String>();
		float cpus = .5f;
		int memMBytes = 500;
		int replicas = 1;
		int targetPort = 27017;
		int publishPort = 27017;
		boolean useProxy = canDeployCodeTo;
		//  Storage Info
		String podPath = "/var/lib/mongodb";
		String pvSubPath = serviceName;
		String pvClaim = LOCAL_PV_CLAIM;	// TODO: Fix why MonoDB fails to work on FAT32 Network drive 				
				
		// Initialize all the Service Info Object Models
		StorageInfo storage = new StorageInfo(shortName + "-pv", podPath, pvSubPath, pvClaim);
		ServiceInfo info = new ServiceInfo(dockerImageName, serviceName, namespace, labels, envs, cpus, memMBytes, replicas, targetPort, publishPort, storage, useProxy);
		
		// Call the Client API to create the Service
		System.out.println("=======> Creating " + longName + " Service......");
		Service service = api.createService(info);
    	System.out.println("=======> " + " Service Name of " + service.getMetadata().getName() 
										+ " in Name Space " + service.getMetadata().getNamespace() 
										+ " with Type of " + service.getSpec().getType()
										+ " with a Cluster IP of " + service.getSpec().getClusterIP() 
										+ " with an External IP of " + ((service.getSpec().getExternalIPs().size() == 0) ? "<none>" : service.getSpec().getExternalIPs().get(0))
										+ " with Port Spec of " + service.getSpec().getPorts().get(0).getPort() + (service.getSpec().getPorts().get(0).getNodePort() == null ? "" : ":" + service.getSpec().getPorts().get(0).getNodePort())  + "/" + service.getSpec().getPorts().get(0).getProtocol());

    	// Copy the default Web Page to the Service Pod
    	if(canDeployCodeTo)
    	{
    		URL resource = Play.class.getClassLoader().getResource("resources/defaults/index.html");
    		if (resource != null)
    		{
    			try 
    			{
    				File htmlFile = new File(resource.toURI());
    				api.copyFileToPod(serviceName, "default", htmlFile, podPath + "/" + serviceName + "/" + "index.html");
    			} 
    			catch (URISyntaxException e) 
    			{
    				logger.info("Deploying default Web Page failed");
    				e.printStackTrace();
    			}
    		}
    		else
    		{
    			logger.info("Could not load index.html file as a resource");
    		}
    	}

    	// Display all the useful information the User needs to know
    	if(useProxy)
    		System.out.println("=======>  Access your application at " + masterNodeIpAddress + ":" + api.getProxyPort() + "/" + serviceName + "/");
    	else
    		System.out.println("=======>  Access your application at " + masterNodeIpAddress + ":" + service.getSpec().getPorts().get(0).getNodePort() + "/" + serviceName + "/");   		
    	if(pvClaim.equals(LOCAL_PV_CLAIM))
    	{
    		System.out.println("=======>  Find your Node by listing all the Pods and searching for one that starts with: " + serviceName + "-deployment");
    		if(canDeployCodeTo)
    			System.out.println("=======>  Your code will be deployed to " + "/cloudsapps/local/" + pvSubPath + "/" + serviceName);
    		else
    			System.out.println("=======>  Your application is located at " + "/cloudsapps/local/" + pvSubPath);
    	}
    	else
    	{
    		if(canDeployCodeTo)
    			System.out.println("=======>  Your code will be deployed to " + "smb//10.0.0.133/share/" + pvSubPath + "/" + serviceName);
    		else
    			System.out.println("=======>  Your application is located at " + "smb//10.0.0.133/share/" + pvSubPath);
    	}  
 
    	// Return all OK
    	return true;
	}
	
	private static boolean createTbdService()
	{
		// A template for creating any Service. Customize the following variables as you see fit to test out new Stacks
		//  START CUSTOMIZATION
		String longName = "VS Code in a Browser Service";
		String shortName = "vscode";
		String dockerImageName = "ghcr.io/linuxserver/code-server";
		boolean canDeployCodeTo = false;
		float cpus = .5f;
		int memMBytes = 500;
		int replicas = 1;
		int targetPort = 8443;
		int publishPort = 8443;
		String podPath = "/config";
		String pvClaim = NETWORK_PV_CLAIM; 
		HashMap<String, String> envs = new HashMap<String, String>();
/*		
		String longName = "Custom PHP 7.1 Web App Server";
		String shortName = "php";
		String dockerImageName = "markreha/php:7.1";
		boolean canDeployCodeTo = true;
		float cpus = .5f;
		int memMBytes = 500;
		int replicas = 1;
		int targetPort = 80;
		int publishPort = 80;
		String podPath = "/etc/phpmyadmin/config.user.inc.php";
		String pvClaim = NETWORK_PV_CLAIM; 
		HashMap<String, String> envs = new HashMap<String, String>();
*/		
		//  END CUSTOMIZATION
		
		// Set the Service Info (this would normally come from the database)
		//  Service Info
		String userId = "0";
		String serviceName = shortName + userId + "-" + System.currentTimeMillis();
		String namespace = "default";
		HashMap<String, String> labels = new HashMap<String, String>();
			labels.put("app", shortName);
			labels.put("environment", "pi");
			labels.put("owner", userId);
		boolean useProxy = canDeployCodeTo;
		//  Storage Info
		String pvSubPath = serviceName;

		// Initialize all the Service Info Object Models
		StorageInfo storage = new StorageInfo(shortName + "-pv", podPath, pvSubPath, pvClaim);
		ServiceInfo info = new ServiceInfo(dockerImageName, serviceName, namespace, labels, envs, cpus, memMBytes, replicas, targetPort, publishPort, storage, useProxy);
		
		// Call the Client API to create the Service
		System.out.println("=======> Creating " + longName + " Service......");
		Service service = api.createService(info);
    	System.out.println("=======> " + " Service Name of " + service.getMetadata().getName() 
										+ " in Name Space " + service.getMetadata().getNamespace() 
										+ " with Type of " + service.getSpec().getType()
										+ " with a Cluster IP of " + service.getSpec().getClusterIP() 
										+ " with an External IP of " + ((service.getSpec().getExternalIPs().size() == 0) ? "<none>" : service.getSpec().getExternalIPs().get(0))
										+ " with Port Spec of " + service.getSpec().getPorts().get(0).getPort() + (service.getSpec().getPorts().get(0).getNodePort() == null ? "" : ":" + service.getSpec().getPorts().get(0).getNodePort())  + "/" + service.getSpec().getPorts().get(0).getProtocol());

    	// Copy the default Web Page to the Service Pod
    	if(canDeployCodeTo)
    	{
    		URL resource = Play.class.getClassLoader().getResource("resources/defaults/index.html");
    		if (resource != null)
    		{
    			try 
    			{
    				File htmlFile = new File(resource.toURI());
    				api.copyFileToPod(serviceName, "default", htmlFile, podPath + "/" + serviceName + "/" + "index.html");
    			} 
    			catch (URISyntaxException e) 
    			{
    				logger.info("Deploying default Web Page failed");
    				e.printStackTrace();
    			}
    		}
    		else
    		{
    			logger.info("Could not load index.html file as a resource");
    		}
    	}

    	// Display all the useful information the User needs to know
    	if(useProxy)
    	{
    		System.out.println("=======>  Access your application at " + masterNodeIpAddress + ":" + api.getProxyPort() + "/" + serviceName + "/");
    	}
    	else
    	{
    		if(canDeployCodeTo)
    			System.out.println("=======>  Access your application at " + masterNodeIpAddress + ":" + service.getSpec().getPorts().get(0).getNodePort() + "/" + serviceName + "/"); 
    		else
       			System.out.println("=======>  Access your application at " + masterNodeIpAddress + ":" + service.getSpec().getPorts().get(0).getNodePort() + "/"); 
    		     	}
    	if(pvClaim.equals(LOCAL_PV_CLAIM))
    	{
    		System.out.println("=======>  Find your Node by listing all the Pods and searching for one that starts with: " + serviceName + "-deployment");
    		if(canDeployCodeTo)
    			System.out.println("=======>  Your code will be deployed to " + "/cloudsapps/local/" + pvSubPath + "/" + serviceName);
    		else
    			System.out.println("=======>  Your application is located at " + "/cloudsapps/local/" + pvSubPath);
    	}
    	else
    	{
    		if(canDeployCodeTo)
    			System.out.println("=======>  Your code will be deployed to " + "smb//10.0.0.133/share/" + pvSubPath + "/" + serviceName);
    		else
    			System.out.println("=======>  Your application is located at " + "smb//10.0.0.133/share/" + pvSubPath);
    	}  
 
    	// Return all OK
    	return true;		
	}

	private static boolean deleteDeployment()
	{
		// Display all Deployments and prompt the user for which Deployment from Name Space to remove
		if(!listAllDeployments())
			return false;
		System.out.print("Enter Deployment to remove: ");
		String service = reader.next();
		System.out.print("From Namespace: ");
		String namespace = reader.next();
		
		// Strip the -deployment off
		service = service.substring(0, service.indexOf("-deployment"));
		logger.info("Going to delete Deployment of " + service + "-deployment" + " with its Service of " + service + "-service");
		
		// Call the Client API to delete the a Deployment and its Service
		boolean ok = api.deleteDeployment(service, namespace);
		logger.info("Service Deletion Status of " + ok);
		return ok;
	}

	private static boolean deployCodeToDeployment()
	{
		// Display all Deployments and prompt the user for which Deployment from Name Space to deploy code to
		if(!listAllDeployments())
			return false;
		System.out.print("Enter Deployment to deploy code to: ");
		String service = reader.next();
		System.out.print("From Namespace: ");
		String namespace = reader.next();
		
		// Strip the -deployment off
		service = service.substring(0, service.indexOf("-deployment"));
		logger.info("Going to deploy code to Deployment of " + service + "-deployment");
		
		// Prompt the user for TarGz file and check for the existance of the file
		System.out.print("Enter complete file path and the TarGz file to deploy: ");
		String fileToDeploy = reader.next();
		if(fileToDeploy.contains("."))
		{
			String extension = fileToDeploy.substring(fileToDeploy.lastIndexOf(".") + 1);
			if(extension.compareToIgnoreCase("gz") != 0)
			{
				System.out.println("=======>  Error: Only TarGz files are can be used to deploy code");
				return false;
			}			
		}
		File fileToUpload = new File(fileToDeploy);
		if(!fileToUpload.isFile())
		{
			System.out.println("=======>  Error: TarGz file does not exist");
			return false;
		}

		// Deploy the TarGz Code file to the Pod in the Deployment
		boolean ok = api.deployCodeToPod(service, namespace, fileToUpload);
		logger.info("Code deployment Status of " + ok);
		return ok;
	}

	private static boolean toggleDefaultName()
	{
		// Toggle whether to display just the Default Namespace or not
		System.out.print("Enter Y to display just Default Namespace or N to display all Namespaces: ");
		String yn = reader.next();
		if(yn.toLowerCase().equals("y"))
			displayDefaultNamespace = true;
		else
			displayDefaultNamespace = false;
		return true;
	}
}
