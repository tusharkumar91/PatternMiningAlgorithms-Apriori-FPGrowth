/**
 * 
 */
package com.uofr.csc440.project.project1.datamodel.formatter;

import java.util.List;
import java.util.Map;

/**
 * Interface for creating a contract 
 * for any kind of formatter for any dataset
 * which takes in a mapping of frequent itemsets 
 * of integral values and their support counts and 
 * prints them in a verbose manner
 * @author tusharkumar
 *
 */
public interface ItemsetFormatter {
	
	/**
	 * Method to set total transactions required to calculate relative support
	 * @param totalTransactions
	 */
	public void setTotalTransactions(int totalTransactions);
	
	/**
	 * Method to actually do the job of formatting the itemsets converting them
	 * from integral lists to desired format and then
	 * printing them to the console.
	 * @param patternMapping
	 */
	public void formatAndPrintItemSets(Map<List<Integer>, Integer> patternMapping);
}
