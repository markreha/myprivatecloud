package com.ontheedgesc.kubernetesapi;

public class StorageInfo
{
	private String name;					// Storage Name
	private String podPath;					// The Path inside the Pod to map
	private String pvSubPath;				// The Sub-path off of the Persistent Volume
	private String pvClaimName;				// The Persistent Volume Claim Name
	
	/**
	 * Non Default Constructor to specify the Service Storage creation information.
	 * 
	 * @param podPath The Path inside the Pod to map
	 * @param pvSubPath The Sub-path off of the Persistent Volume
	 * @param pvClaimName The Persistent Volume Claim Name
	 */
	public StorageInfo(String name, String podPath, String pvSubPath, String pvClaimName)
	{
		super();
		this.name = name;
		this.podPath = podPath;
		this.pvSubPath = pvSubPath;
		this.pvClaimName = pvClaimName;
	}
	
	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getPodPath()
	{
		return podPath;
	}
	public void setPodPath(String podPath)
	{
		this.podPath = podPath;
	}
	public String getPvSubPath()
	{
		return pvSubPath;
	}
	public void setPvSubPath(String pvSubPath)
	{
		this.pvSubPath = pvSubPath;
	}
	public String getPvClaimName()
	{
		return pvClaimName;
	}
	public void setPvClaimName(String pvClaimName)
	{
		this.pvClaimName = pvClaimName;
	}
}
