package com.uofr.csc440.project.project1.datamodel;

/**
 * Interface for Dataloader. This allows clients
 * to use it on any kind of dataset as long they adhere to
 * the specified contract.
 * @author tusharkumar
 *
 */
public interface DataLoader {
	
	/**
	 * Main method to load the data 
	 * present at the specified path
	 * @param filepath
	 * @return
	 */
	public Dataset loadData(String filepath);
}
