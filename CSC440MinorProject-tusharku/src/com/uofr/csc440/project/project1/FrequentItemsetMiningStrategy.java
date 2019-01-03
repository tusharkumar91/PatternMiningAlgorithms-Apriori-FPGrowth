package com.uofr.csc440.project.project1;

import java.util.List;
import java.util.Map;

import com.uofr.csc440.project.project1.datamodel.Dataset;

/**
 * Interface the build the contract of any 
 * frequent itemset mining algorithm.
 * Having this strategy ensures that one can
 * build programs agnostic of what strategy will be 
 * used at run time.
 * @author tusharkumar
 *
 */
public abstract class FrequentItemsetMiningStrategy {
	protected int totalTransactionScans = 0;
	
	/**
	 * Method to mine all frequent patterns
	 * @param minSupport
	 * @param dataset
	 * @return
	 */
	public abstract Map<List<Integer>, Integer> mineAllFrequentPattern(double minSupport, Dataset dataset);
	
	/**
	 * Method to mine only patterns which have maximum size of K
	 * @param k
	 * @param minSupport
	 * @param dataset
	 * @return
	 */
	public abstract Map<List<Integer>, Integer> mineKFrequentPattern(int k, double minSupport, Dataset dataset);
	
	/**
	 * Method to return the confidence rules given the frequent item sets generated
	 * @param target
	 * @param minConfidence
	 * @return
	 */
	public abstract Map<List<Integer>, Double> getConfidenceRules(int target, double minConfidence);
	
	public int getTotalTransactionScans() {
		return totalTransactionScans;
	}

}
