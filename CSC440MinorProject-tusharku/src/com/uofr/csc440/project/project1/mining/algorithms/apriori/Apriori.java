package com.uofr.csc440.project.project1.mining.algorithms.apriori;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.uofr.csc440.project.project1.FrequentItemsetMiningStrategy;
import com.uofr.csc440.project.project1.datamodel.Dataset;
import com.uofr.csc440.project.project1.datamodel.Row;

/**
 * Class for encapsulating the Apriori Algorithm.
 * The general steps followed are:
 * 1. generate single frequent item sets
 * 2. Generate candidate item sets for next higher size
 * 3. Prune all candidates whose subsets were not frequent
 * 4. Find all candidates passing minimum support threshold
 * 5. Repeat 2-4 till you keep finding itemsets
 * @author tusharkumar
 */
public class Apriori extends FrequentItemsetMiningStrategy {
	
	private List<List<List<Integer>>> itemSets = new ArrayList<List<List<Integer>>>();	
	protected Map<List<Integer>, Integer> patternMapping = new HashMap<List<Integer>, Integer>();
	public List<Map<List<Integer>, Boolean>> itemSetsCache = new ArrayList<Map<List<Integer>, Boolean>>();
	
	public int getTotalTransactionScans() {
		return totalTransactionScans;
	}	

	/**
	 * Method to generate singular itemsets that are meeting the
	 * minimum given transaction absolute support in the input
	 * dataset.
	 * @param minimumTransactionCount
	 * @param dataset
	 * @return
	 */
	private List<List<Integer>> mineSingularFrequentItemSets(int minimumTransactionCount, Dataset dataset){		
		List<List<Integer>> oneItemSet = new ArrayList<List<Integer>>();
		Map<Integer, Integer> countOfItems = new HashMap<Integer, Integer>();
		Map<Integer, Boolean> itemsMap = new HashMap<Integer, Boolean>();
		List<Row> tuples = dataset.getTuples();
		for(Row tuple: tuples) {
			totalTransactionScans++;
			for(Integer item : tuple.getAttributeValues()) {
				int count = countOfItems.getOrDefault(item, 0) + 1;
				countOfItems.put(item, count);
				if(count >= minimumTransactionCount) {
					if(!itemsMap.containsKey(item)) {
						oneItemSet.add(Collections.singletonList(item));
					}
					List<Integer> singleItemList = new ArrayList<Integer>();
					singleItemList.add(item);
					patternMapping.put(singleItemList, count);
					itemsMap.put(item, true);
				}
			}
		}
		return oneItemSet;
	}
	
	/**
	 * Helper Method to generate all itemsets of size k that
	 * are frequent.
	 * @param k
	 * @param minimumTransactionCount
	 * @param dataset
	 * @return
	 */
	private List<List<Integer>> mineKthFrequentPattern(int k, int minimumTransactionCount, Dataset dataset) {
		if(k == 1) {
			return mineSingularFrequentItemSets(minimumTransactionCount, dataset);			
		}
		else {
			List<List<Integer>> kthItemSetCandidates = generateCandidatesForKthItemSet(k);
			List<List<Integer>> kthItemSetCandidatesAfterPruning = getPrunedCandidates(k, kthItemSetCandidates);
			List<List<Integer>> kthItemSet = getCandidatesPassingSupport(minimumTransactionCount, kthItemSetCandidatesAfterPruning, dataset);
			return kthItemSet;
		}
	}
	
	/**
	 * Helper method to prune the candidates by removing
	 * all itemsets a subset of which is themself not frequent.
	 * @param k
	 * @param kthItemSetCandidates
	 * @return
	 */
	private List<List<Integer>> getPrunedCandidates(int k, List<List<Integer>> kthItemSetCandidates) {
		List<List<Integer>> kthItemSetCandidatesAfterPruning = new ArrayList<List<Integer>>();
		/*
		 * For each candidate generate the different possible
		 * subsets and if any one of them is not frequent
		 * which we check through our cache, 
		 * then lets get rid of this candidate
		 */
		for(List<Integer> candidates : kthItemSetCandidates) {	
			boolean foundNonFrequentSubSet = false;
			List<List<Integer>> combinations = new ArrayList<>();
			List<Integer> tempCandidates = new ArrayList<Integer>(candidates);
			getSubsets(tempCandidates, combinations);
			
			for(List<Integer> kMinus1thItemSetCandidate : combinations) {
				int itemSetSize = kMinus1thItemSetCandidate.size();
				if(itemSetSize == k) {
					continue;
				}
				if(!itemSetsCache.get(itemSetSize-1).containsKey(kMinus1thItemSetCandidate)){
					foundNonFrequentSubSet = true;
					break;
				}
			}
			// All subsets were frequent then lets add it
			if(!foundNonFrequentSubSet) {
				kthItemSetCandidatesAfterPruning.add(candidates);
			}
		}
		return kthItemSetCandidatesAfterPruning;
	}

	/**
	 * Utility method to generate all subsets of a given 
	 * list of integers.
	 * Used https://theproductiveprogrammer.blog/GeneratingCombinations.java.php
	 * as reference 
	 * @param itemSet
	 * @param combinations
	 */
	private void getSubsets(List<Integer> itemSet, List<List<Integer>> combinations){
		if(itemSet==null || itemSet.size()==0) {
			return;
		}
		int itemSetSize = itemSet.size();
		for(int number = 1;number<Math.pow(2, itemSetSize);number++){
			String bitRepresentation = Integer.toBinaryString(number);
			List<Integer> combination = new ArrayList<>();
			for(int idx = 0; idx < bitRepresentation.length();idx++){
				//Is this bit set
				if(bitRepresentation.charAt(idx)=='1'){
					int itemSetIndexToAdd = itemSetSize-bitRepresentation.length()+idx;
					combination.add(itemSet.get(itemSetIndexToAdd));
				}
			}
			combinations.add(combination);
		}
	}

	/**
	 * Utility method to generate all candidate itemsets of size K
	 * by iterating through already generated itemsets
	 * of size K-1
	 * @param k
	 * @return
	 */
	private List<List<Integer>> generateCandidatesForKthItemSet(int k){
		List<List<Integer>> candidates = itemSets.get(k-2);
		List<List<Integer>> kthItemSetCandidates = new ArrayList<List<Integer>>();
		Map<Set<Integer>, Boolean> itemSetGenerated = new HashMap<Set<Integer>, Boolean>();
		for(int i=0; i<candidates.size()-1; i++) {
			Set<Integer> leftCandidate = new HashSet<Integer>(candidates.get(i));
			for(int j=i+1; j<candidates.size(); j++) {
				Set<Integer> rightCandidate = new HashSet<Integer>(candidates.get(j));
				Set<Integer> intersection = new HashSet<Integer>(rightCandidate);
				intersection.retainAll(leftCandidate);
				if(intersection.size() == leftCandidate.size()-1) {
					Set<Integer> union = new HashSet<Integer>(leftCandidate);
					union.addAll(rightCandidate);
					if(!itemSetGenerated.containsKey(union)) {
						List<Integer> unionList = new ArrayList<>(union);
						kthItemSetCandidates.add(unionList);
						itemSetGenerated.put(union, true);
					}
				}
				
			}
		}
		return kthItemSetCandidates;
	}
	
	/**
	 * Method to filter out the candidates which we generate 
	 * by actually scanning the database and finding that their
	 * support is meeting the minimum criteria.
	 * @param minimumTransactionCount
	 * @param kthItemSetCandidatesAfterPruning
	 * @param dataset
	 * @return
	 */
	protected List<List<Integer>> getCandidatesPassingSupport(int minimumTransactionCount,
			List<List<Integer>> kthItemSetCandidatesAfterPruning, Dataset dataset) {
		List<List<Integer>> kthItemSet = new ArrayList<List<Integer>>();
		List<Row> tuples = dataset.getTuples();
		Map<Integer, Integer> itemSetToFrequencyMapping = new HashMap<Integer, Integer>();
		
		for(int i=0; i<tuples.size();i++) {
			Row tuple = tuples.get(i);
			totalTransactionScans++;
			for(int j = 0; j < kthItemSetCandidatesAfterPruning.size(); j++){
				List<Integer> candidateItemSet = kthItemSetCandidatesAfterPruning.get(j);
				List<Integer> attributeValues = tuple.getAttributeValues();
				if(attributeValues.containsAll(candidateItemSet)){
					itemSetToFrequencyMapping.put(j, (itemSetToFrequencyMapping.getOrDefault(j, 0))+1);	
				}		
			}
		}		
		for(int j = 0; j < kthItemSetCandidatesAfterPruning.size(); j++){
			if(itemSetToFrequencyMapping.containsKey(j) && (itemSetToFrequencyMapping.get(j) >= minimumTransactionCount)) {
				kthItemSet.add(kthItemSetCandidatesAfterPruning.get(j));
				patternMapping.put(kthItemSetCandidatesAfterPruning.get(j), itemSetToFrequencyMapping.get(j));
			}
		}
		return kthItemSet;
	}	
	
	/**
	 * Main method that should be called to generate all frequent 
	 * itemsets meeting the minimum relative support criteria.
	 */
	@Override
	public Map<List<Integer>, Integer> mineAllFrequentPattern(double minSupport, Dataset dataset) {
		int numberOfTransactions = dataset.getTuples().size();
		int minimumTransactionCount = (int) (numberOfTransactions * minSupport);
		for(int k=1;;k++) {
			if(dataset.getTuples().isEmpty()) {
				break;
			}
			List<List<Integer>> itemSet = mineKthFrequentPattern(k, minimumTransactionCount, dataset);
			Map<List<Integer>, Boolean> kthItemSetCache = new HashMap<List<Integer>, Boolean>();
			for(List<Integer> itemSetMember : itemSet) {
				kthItemSetCache.put(itemSetMember, true);
			}
			itemSetsCache.add(kthItemSetCache);
			if(itemSet == null || itemSet.isEmpty()) {
				break;
			}
			else {
				itemSets.add(itemSet);
			}
		}	
		return patternMapping;
	}
	
	/**
	 * Overridden method to find all itemsets only till a specific K
	 */
	@Override
	public Map<List<Integer>, Integer> mineKFrequentPattern(int maxK, double minSupport, Dataset dataset) {
		int numberOfTransactions = dataset.getTuples().size();
		int minimumTransactionCount = (int) (numberOfTransactions * minSupport);
		for(int k=1; k<=maxK ;k++) {
			if(dataset.getTuples().isEmpty()) {
				break;
			}
			List<List<Integer>> itemSet = mineKthFrequentPattern(k, minimumTransactionCount, dataset);
			Map<List<Integer>, Boolean> kthItemSetCache = new HashMap<List<Integer>, Boolean>();
			for(List<Integer> itemSetMember : itemSet) {
				kthItemSetCache.put(itemSetMember, true);
			}
			itemSetsCache.add(kthItemSetCache);
			if(itemSet == null || itemSet.isEmpty()) {
				break;
			}
			else {
				itemSets.add(itemSet);
			}
		}	
		return patternMapping;
	}
	
	/**
	 * Method to generate the confidence rules given the target integral value
	 * in the dataset and the minimum desired confidence
	 */
	public Map<List<Integer>, Double> getConfidenceRules(int target, double minConfidence) {
		Map<List<Integer>, Double> ruleConfidenceMapping = new HashMap<List<Integer>, Double>();
		for(List<Integer> itemSets : patternMapping.keySet()) {
			if(itemSets.contains(target)) {
				double supportUnion = patternMapping.get(itemSets);
				List<Integer> antecedent = new ArrayList<Integer>(itemSets);
				antecedent.remove(Integer.valueOf(target));
				if(patternMapping.containsKey(antecedent)) {
					double confidence = supportUnion / (patternMapping.get(antecedent));
					if(confidence > minConfidence) {
						ruleConfidenceMapping.put(antecedent, confidence);
					}
				}
			}
		}
		return ruleConfidenceMapping;
	}
}
