/********************************************************
 *  
 *   Copyright 2021 On The Edge Software Consulting LLC.
 *   All rights reserved.
 *   
 */
package com.ontheedgesc.portal.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import org.springframework.core.io.ClassPathResource;

/**
 * Helper class to load Resources from project.
 * 
 * @author markreha
 *
 */
public class ResourceLoader
{
	/**
	 * Reads a Resource and returns a Buffered Reader to read from.
	 * 
	 * @param resourceName Resource name to read
	 * @return Buffered Reader to read the resource
	 * @throws IOException if something goes bad
	 */
	public static BufferedReader getAsReader(String resourceName) throws IOException
	{
		return new BufferedReader(new InputStreamReader(new ClassPathResource(resourceName).getInputStream()));
	}
	
	/**
	 * Reads a Resource and returns a tempory File to read from.
	 * 
	 * @param resourceName Resource name to read
	 * @return File to read the resource
	 * @throws IOException if something goes bad
	 */
	public static File getAsFile(String resourceName) throws IOException
	{
		BufferedReader reader = new BufferedReader(new InputStreamReader(new ClassPathResource(resourceName).getInputStream()));
		File tmpFile = File.createTempFile("tmp", "txt");
		PrintWriter writer = new PrintWriter(tmpFile);
		String line  = "";
		while((line = reader.readLine()) != null)
		{
			writer.write(line + "\n");
		}
		writer.close();
		return tmpFile;
	}
}
