package com.ontheedgesc.kubernetesapi;

import java.util.Map;

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
	 * Non Default Constructor to specify the Service creation information.
	 * 
	 * @param image The Image Name.
	 * @param name The Service Name (should be made unique).
	 * @param cpus The CPU's in a percent of a core, this is converted to millicores (multiplied by 1000 in the getter).
	 * @param memMBytes The Memory in Mbytes, this is converted to bytes internally (multiplied by 1000000 in the getter).
	 * @param replicas The number of replicas to create in the Swarm.
	 * @param targetPort The Target Port inside the Container.
	 * @param publishPort The Published Port outside the Container.
	 * @param storageInfo Configuration for the Service Storage
	 * @param useIngress Whether to use NodePorts or ClusterIP(i.e. Reverse Proxy)
	 */
	public ServiceInfo(String image, String name, String namespace, Map<String, String> labels, Map<String, String> envs, float cpus, int memMBytes, int replicas, int targetPort, int publishPort, StorageInfo storageInfo, boolean useIngress)
	{
		super();
		this.image = image;
		this.name = name;
		this.namespace = namespace;
		this.labels = labels;
		this.envs = envs;
		this.cpus = cpus;
		this.memMBytes = memMBytes;
		this.replicas = replicas;
		this.targetPort = targetPort;
		this.publishPort = publishPort;
		this.storageInfo = storageInfo;
		this.useIngress = useIngress;
	}

	public String getImage()
	{
		return image;
	}

	public void setImage(String image)
	{
		this.image = image;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
	
	public void setNamespace(String namespace)
	{
		this.namespace = namespace;
	}

	public String getNamespace()
	{
		return namespace;
	}

	public void setLabels(Map<String, String> labels)
	{
		this.labels = labels;
	}

	public Map<String, String> getLabels()
	{
		return labels;
	}

	public void setEnvs(Map<String, String> envs)
	{
		this.envs = envs;
	}

	public Map<String, String> getEnvs()
	{
		return envs;
	}

	public float getCpus()
	{
		return cpus * 1000;
	}

	public void setCpus(float cpus)
	{
		this.cpus = cpus;
	}

	public int getMemMBytes()
	{
		return memMBytes;
	}

	public void setMemMBytes(int memMBytes)
	{
		this.memMBytes = memMBytes;
	}

	public int getReplicas()
	{
		return replicas;
	}

	public void setReplicas(int replicas)
	{
		this.replicas = replicas;
	}

	public int getTargetPort()
	{
		return targetPort;
	}

	public void setTargetPort(int targetPort)
	{
		this.targetPort = targetPort;
	}

	public int getPublishPort()
	{
		return publishPort;
	}

	public void setPublishPort(int publishPort)
	{
		this.publishPort = publishPort;
	}

	public StorageInfo getStorageInfo()
	{
		return storageInfo;
	}

	public void setStorageInfo(StorageInfo storageInfo)
	{
		this.storageInfo = storageInfo;
	}
	
	public boolean isUseIngress() 
	{
		return useIngress;
	}

	public void setUseIngress(boolean useIngress) 
	{
		this.useIngress = useIngress;
	}
}
