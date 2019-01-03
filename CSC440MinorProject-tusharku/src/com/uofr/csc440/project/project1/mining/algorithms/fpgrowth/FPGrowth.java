package com.uofr.csc440.project.project1.mining.algorithms.fpgrowth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.uofr.csc440.project.project1.FrequentItemsetMiningStrategy;
import com.uofr.csc440.project.project1.datamodel.Dataset;
import com.uofr.csc440.project.project1.datamodel.Row;



/**
 * Class for encapsulating the FPGrowth Algorithm.
 * The general steps followed are:
 * 1. Generate all single frequent itemsets
 * 2. Build a FPtree with these itemsets and there support counts
 * 3. Mine these FPtrees in a recursive manner.
 * @author tusharkumar
 *
 */
public class FPGrowth extends FrequentItemsetMiningStrategy {
	
	//Map of all itemsets to respective count
	private Map<List<Integer>, Integer> patternMapping = new HashMap<List<Integer>, Integer>();
	int maxK = 0;
	

	/**
	 * Mining each FPtree created in a recursive manner
	 * @param fpTreeNode
	 * @param alpha
	 * @param headerTable
	 * @param minSupport
	 * @return
	 */
	private void recursiveFPGrowth(FPTreeNode fpTreeNode, List<Integer> alpha, Map<Integer, FPTreeNode> headerTable, double minSupport) {
		if(isSinglePathTree(fpTreeNode)) {
			getAllPatterns(fpTreeNode, alpha);
		}
		List<FPTreeNode> headerTableValues = new ArrayList<FPTreeNode>(headerTable.values());

		Collections.sort(headerTableValues, new Comparator<FPTreeNode>() {

			@Override
			public int compare(FPTreeNode o1, FPTreeNode o2) {
				int diffCount = o2.getItemFrequency() - o1.getItemFrequency();
				return (diffCount != 0) ? diffCount : (o1.getItemValue() - o2.getItemValue());
			}
		});
		
		for(FPTreeNode headerValue : headerTableValues) {
			//Generate pattern beta = alphai Union alpha with support count=alphai.support count;
			List<Integer> alphaI = Collections.singletonList(headerValue.getItemValue());
			List<Integer> betaPattern = new ArrayList<Integer>(alphaI);
			if(alpha != null && !alpha.isEmpty()) {
				List<Integer> alphaCopy = new ArrayList<Integer>(alpha);
				betaPattern.addAll(alphaCopy);
			}	
			
			//Keep a check of the maximum sized pattern found
			maxK = Math.max(maxK, betaPattern.size());
			patternMapping.put(betaPattern, headerValue.getItemFrequency());
			
			/*
			 * Build the conditional pattern base
			 */
			Map<List<Integer>, Integer> conditionalPatternBase = buildConditionalPatternBase(headerValue);
			
			/*
			 * Build and mine the conditional fptree created
			 */
			Map<Integer, Integer> frequentItemToFrequencyMapping = buildConditionalFrequentItemToFrequencyMapping(conditionalPatternBase, minSupport);			
			if(!frequentItemToFrequencyMapping.keySet().isEmpty()) {
				Map<Integer, FPTreeNode> newHeaderTable = new HashMap<Integer, FPTreeNode>();
				FPTreeNode fpConditionalTreeNode = getConditionalFPTree(frequentItemToFrequencyMapping, conditionalPatternBase, newHeaderTable);
				if(fpConditionalTreeNode !=null && 
						fpConditionalTreeNode.getChildNodes() != null && 
						fpConditionalTreeNode.getChildNodes().keySet().size() > 0) {
					recursiveFPGrowth(fpConditionalTreeNode, betaPattern, newHeaderTable, minSupport);
				}
			}
		}		
	}

	/**
	 * Method to build the conditional pattern base
	 * given the current header value which points 
	 * to a node.
	 * @param headerValue
	 * @return
	 */
	private Map<List<Integer>, Integer> buildConditionalPatternBase(FPTreeNode headerValue) {
		Map<List<Integer>, Integer> conditionalPatternBase = new HashMap<>();			
		FPTreeNode siblingNode = headerValue.getNextSiblingNode();
		while(siblingNode != null) {
			int support = siblingNode.getItemFrequency();
			List<Integer> pathFromRootToNode = new ArrayList<Integer>();	
			FPTreeNode parentNode = siblingNode.getparentNode();
			while(!parentNode.isRootNode) {
				pathFromRootToNode.add(parentNode.itemValue);
				parentNode = parentNode.getparentNode();
			}
			if(!pathFromRootToNode.isEmpty()) {
				Collections.reverse(pathFromRootToNode);
				conditionalPatternBase.put(pathFromRootToNode, support);
			}
			siblingNode = siblingNode.getNextSiblingNode();
		}
		return conditionalPatternBase;
	}

	/**
	 * Get all possible patterns that can be formed
	 * given a single path from root to leaf.
	 * @param fpTreeNode
	 * @param alpha
	 */
	private void getAllPatterns(FPTreeNode fpTreeNode, List<Integer> alpha) {
		List<FPTreeNode> singluarPath = new ArrayList<FPTreeNode>(); 
		Map<Integer, FPTreeNode> childNodes = fpTreeNode.getChildNodes();
        while (childNodes != null && !childNodes.isEmpty()) {
            Collection<FPTreeNode> children = childNodes.values();
            FPTreeNode child = children.iterator().next();
            singluarPath.add(child);
            childNodes = child.getChildNodes();
        }
        
        List<List<FPTreeNode>> combinations = new ArrayList<>();
        getSubsets(singluarPath, combinations); 
        for(List<FPTreeNode> pattern : combinations) {
        	int minSupport = Integer.MAX_VALUE;        	
        	List<Integer> alphaPattern = new ArrayList<Integer>();
        	if(alpha != null) {
        		alphaPattern = new ArrayList<Integer>(alpha);
        	}
        	List<Integer> betaPattern = new ArrayList<Integer>();
        	for(FPTreeNode node : pattern) {
        		minSupport = Math.min(minSupport, node.getItemFrequency());
        		betaPattern.add(node.getItemValue());
        	}
        	betaPattern.addAll(alphaPattern);
        	maxK = Math.max(maxK, betaPattern.size());
        	patternMapping.put(betaPattern, minSupport);
        }
		
	}

	/**
	 * Utility method to generate all subsets of a given 
	 * list of integers.
	 * Used https://theproductiveprogrammer.blog/GeneratingCombinations.java.php
	 * as reference
	 * @param itemSet
	 * @param combinations
	 */
	private void getSubsets(List<FPTreeNode> itemSet, List<List<FPTreeNode>> combinations){
		if(itemSet==null || itemSet.size()==0) {
			return;
		}
		int itemSetSize = itemSet.size();
		for(int number = 1;number<Math.pow(2, itemSetSize);number++){
			String bitRepresentation = Integer.toBinaryString(number);
			List<FPTreeNode> combination = new ArrayList<>();
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
	 * Method to check if the tree is one which has only
	 * a single path from root to leaf
	 * @param fpTreeNode
	 * @return
	 */
	private boolean isSinglePathTree(FPTreeNode fpTreeNode) {
		boolean isSinglePath = true;
		Map<Integer, FPTreeNode> childNodes = fpTreeNode.getChildNodes();
        while (childNodes != null && !childNodes.isEmpty()) {
            Collection<FPTreeNode> children = childNodes.values();
            if(children.size() > 1) {
            	isSinglePath = false;
            	break;
            }
            else{
            	FPTreeNode child = children.iterator().next();
            	childNodes = child.getChildNodes();
            }
        }
        return isSinglePath;
	}

	/**
	 * Method to generate the conditional FPtree given 
	 * the conditional pattern base. Also updates
	 * the header tbale in place.
	 * @param frequentItemToFrequencyMapping
	 * @param conditionalPatternBase
	 * @param headerTable
	 * @return
	 */
	private FPTreeNode getConditionalFPTree(Map<Integer, Integer> frequentItemToFrequencyMapping,
			Map<List<Integer>, Integer> conditionalPatternBase, Map<Integer, FPTreeNode> headerTable) {
		List<Integer> frequentItems = new ArrayList<Integer>(frequentItemToFrequencyMapping.keySet());
		Collections.sort(frequentItems, new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				int diffCount = frequentItemToFrequencyMapping.get(o2) - frequentItemToFrequencyMapping.get(o1);
				return (diffCount != 0) ? diffCount : (o1 - o2);
			}
		});
		FPTreeNode fpTreeRoot = new FPTreeNode(-1, -1, null);		
		fpTreeRoot.setRootNode(true);
		for(Map.Entry<List<Integer>, Integer> entry : conditionalPatternBase.entrySet()) {			
			List<Integer> frequentItemForTuple = new ArrayList<Integer>();
			for(Integer item : entry.getKey()) {
				if(frequentItemToFrequencyMapping.containsKey(item)) {
					frequentItemForTuple.add(item);
				}
			}
			Collections.sort(frequentItemForTuple, new Comparator<Integer>() {
				@Override
				public int compare(Integer o1, Integer o2) {
					int diffCount = frequentItemToFrequencyMapping.get(o2) - frequentItemToFrequencyMapping.get(o1);
					return (diffCount != 0) ? diffCount : (o1 - o2);
				}
			});
			int supportCount = entry.getValue();
			insertConditionalTree(frequentItemForTuple, supportCount, fpTreeRoot, headerTable);
		}		
		return fpTreeRoot;
	}

	/**
	 * Recursive method to insert items in a conditional
	 * tree one by one and updating the header table at
	 * each insert
	 * @param frequentItemForTuple
	 * @param supportCount
	 * @param fpTreeRoot
	 * @param headerTable
	 */
	private void insertConditionalTree(List<Integer> frequentItemForTuple, int supportCount, FPTreeNode fpTreeRoot,
			Map<Integer, FPTreeNode> headerTable) {
		if(frequentItemForTuple.isEmpty()) {
			return;
		}
		int firstElement = frequentItemForTuple.get(0);
		FPTreeNode newRoot = null;
		if(fpTreeRoot.getChildNodes() != null) {
			if(fpTreeRoot.getChildNodes().containsKey(firstElement)) {
				newRoot = fpTreeRoot.getChildNodes().get(firstElement);
				newRoot.setItemFrequency(newRoot.getItemFrequency()+supportCount);	
				updateHeaderTable(newRoot, headerTable, supportCount);
			}
			else {
				newRoot = new FPTreeNode(firstElement, supportCount, fpTreeRoot);
				fpTreeRoot.getChildNodes().put(firstElement, newRoot);
				updateHeaderTable(newRoot, headerTable, supportCount);
			}
		}
		else {
			Map<Integer, FPTreeNode> childNodes = new HashMap<Integer, FPTreeNode>();
			newRoot = new FPTreeNode(firstElement, supportCount, fpTreeRoot);
			childNodes.put(firstElement, newRoot);
			fpTreeRoot.setChildNodes(childNodes);
			updateHeaderTable(newRoot, headerTable, supportCount);
		}
		frequentItemForTuple.remove(0);
		insertConditionalTree(frequentItemForTuple, supportCount, newRoot, headerTable);
		
	}

	/**
	 * Method to update the header table during 
	 * insertion of nodes in the fptree
	 * @param newRoot
	 * @param headerTable
	 * @param supportCount
	 */
	private void updateHeaderTable(FPTreeNode newRoot, Map<Integer, FPTreeNode> headerTable, int supportCount) {
		if(headerTable.containsKey(newRoot.getItemValue())) {
			FPTreeNode headerNode = headerTable.get(newRoot.getItemValue());
			headerNode.setItemFrequency(headerNode.getItemFrequency()+supportCount);
			if(!(headerNode.getNextSiblingNode() == newRoot)) {
				while(headerNode.getNextSiblingNode() != null) {
					headerNode = headerNode.getNextSiblingNode();
					if(headerNode == newRoot) {
						return;
					}
				}
				headerNode.setNextSiblingNode(newRoot);
			}
		}
		else {
			FPTreeNode headerNode = new FPTreeNode(newRoot.getItemValue(), supportCount, null);
			headerNode.setNextSiblingNode(newRoot);
			headerTable.put(newRoot.getItemValue(), headerNode);
		}
		
	}

	/**
	 * Method to generate the itemset to support mapping
	 * given a conditional pattern base
	 * @param conditionalPatternBase
	 * @param minSupport
	 * @return
	 */
	private Map<Integer, Integer> buildConditionalFrequentItemToFrequencyMapping(
			Map<List<Integer>, Integer> conditionalPatternBase, double minSupport) {
		Map<Integer, Integer> frequentItemToFrequencyMapping = new HashMap<Integer, Integer>();
		for(Map.Entry<List<Integer>, Integer> entry : conditionalPatternBase.entrySet()) {
			for(Integer item : entry.getKey()) {
				if(frequentItemToFrequencyMapping.containsKey(item)) {
					frequentItemToFrequencyMapping.put(item, 
							frequentItemToFrequencyMapping.get(item) + entry.getValue());					
				}
				else {
					frequentItemToFrequencyMapping.put(item, entry.getValue());
				}
			}
		}
		Map<Integer, Integer> frequentItemToFrequencyMappingCopy = new HashMap<Integer, Integer>(frequentItemToFrequencyMapping);
		for(Map.Entry<Integer, Integer> entry : frequentItemToFrequencyMappingCopy.entrySet()) {
			if(entry.getValue() < minSupport) {
				frequentItemToFrequencyMapping.remove(entry.getKey());
			}
		}
		return frequentItemToFrequencyMapping;
	}

	/**
	 * Method to build the FPTree. This is ONLY called for the first time.
	 * Post that we use the conditional pattern building methods
	 * both tree building
	 * @param frequentItemToFrequencyMapping
	 * @param headerTable
	 * @param dataset
	 * @return
	 */
	private FPTreeNode getFPTree(Map<Integer, Integer> frequentItemToFrequencyMapping, Map<Integer, FPTreeNode> headerTable, Dataset dataset) {
		FPTreeNode fpTreeRoot = new FPTreeNode(-1, -1, null);
		fpTreeRoot.setRootNode(true);
		List<Row> tuples = dataset.getTuples();
		for(Row tuple: tuples) {
			totalTransactionScans++;
			List<Integer> frequentItemForTuple = new ArrayList<Integer>();
			for(Integer item : tuple.getAttributeValues()) {
				if(frequentItemToFrequencyMapping.containsKey(item)) {
					frequentItemForTuple.add(item);
				}
			}
			Collections.sort(frequentItemForTuple, new Comparator<Integer>() {
				@Override
				public int compare(Integer o1, Integer o2) {
					int diffCount = frequentItemToFrequencyMapping.get(o2) - frequentItemToFrequencyMapping.get(o1);
					return (diffCount != 0) ? diffCount : (o1 - o2);
				}
			});
			insertTree(frequentItemForTuple, fpTreeRoot, headerTable);
			//System.out.println(frequentItemForTuple);
		}
		return fpTreeRoot;
	}

	/**
	 * Recursive method to insert items in a FP
	 * tree(NOT conditional) one by one and updating the header table at
	 * each insert
	 * @param frequentItemForTuple
	 * @param supportCount
	 * @param fpTreeRoot
	 * @param headerTable
	 */
	private void insertTree(List<Integer> frequentItemForTuple, FPTreeNode fpTreeRoot,
			Map<Integer, FPTreeNode> headerTable) {
		if(frequentItemForTuple.isEmpty()) {
			return;
		}
		int firstElement = frequentItemForTuple.get(0);
		FPTreeNode newRoot = null;
		if(fpTreeRoot.getChildNodes() != null) {
			if(fpTreeRoot.getChildNodes().containsKey(firstElement)) {
				newRoot = fpTreeRoot.getChildNodes().get(firstElement);
				newRoot.setItemFrequency(newRoot.getItemFrequency()+1);	
				updateHeaderTable(newRoot, headerTable);
			}
			else {
				newRoot = new FPTreeNode(firstElement, 1, fpTreeRoot);
				fpTreeRoot.getChildNodes().put(firstElement, newRoot);
				updateHeaderTable(newRoot, headerTable);
			}
		}
		else {
			Map<Integer, FPTreeNode> childNodes = new HashMap<Integer, FPTreeNode>();
			newRoot = new FPTreeNode(firstElement, 1, fpTreeRoot);
			childNodes.put(firstElement, newRoot);
			fpTreeRoot.setChildNodes(childNodes);
			updateHeaderTable(newRoot, headerTable);
		}
		frequentItemForTuple.remove(0);
		insertTree(frequentItemForTuple, newRoot, headerTable);
	}

	/**
	 * Method to update the header tbale for the FP created
	 * for the first time.
	 * @param newRoot
	 * @param headerTable
	 */
	private void updateHeaderTable(FPTreeNode newRoot, Map<Integer, FPTreeNode> headerTable) {
		if(headerTable.containsKey(newRoot.getItemValue())) {
			FPTreeNode headerNode = headerTable.get(newRoot.getItemValue());
			headerNode.setItemFrequency(headerNode.getItemFrequency()+1);
			if(!(headerNode.getNextSiblingNode() == newRoot)) {
				while(headerNode.getNextSiblingNode() != null) {
					headerNode = headerNode.getNextSiblingNode();
					if(headerNode == newRoot) {
						return;
					}
				}
				headerNode.setNextSiblingNode(newRoot);
			}
		}
		else {
			FPTreeNode headerNode = new FPTreeNode(newRoot.getItemValue(), 1, null);
			headerNode.setNextSiblingNode(newRoot);
			headerTable.put(newRoot.getItemValue(), headerNode);
		}
		
	}

	/**
	 * Method to get the singular item frequency.
	 * This is the first scan of the dataset.
	 * @param minSupport
	 * @param dataset
	 * @return
	 */
	private Map<Integer, Integer> getFrequentItemToFrequencyMapping(double minSupport, Dataset dataset) {
		int numberOfTransactions = dataset.getTuples().size();
		Map<Integer, Integer> countOfItems = new HashMap<Integer, Integer>();
		Map<Integer, Integer> frequentItemsMap = new HashMap<Integer, Integer>();
		int minimumTransactionCount = (int) (numberOfTransactions * minSupport);
		List<Row> tuples = dataset.getTuples();
		for(Row tuple: tuples) {
			totalTransactionScans++;
			for(Integer item : tuple.getAttributeValues()) {
				int count = countOfItems.getOrDefault(item, 0) + 1;
				countOfItems.put(item, count);
				if(count >= minimumTransactionCount) {
					frequentItemsMap.put(item, count);
				}
			}
		}
		return frequentItemsMap;
	}

	/**
	 * Main method to be called in order to mine all frequent patterns.
	 * This will mine all patters of any size.
	 */
	@Override
	public Map<List<Integer>, Integer> mineAllFrequentPattern(double minSupport, Dataset dataset) {
		Map<Integer, Integer> frequentItemToFrequencyMapping = getFrequentItemToFrequencyMapping(minSupport, dataset);
		List<Integer> frequentItems = new ArrayList<Integer>(frequentItemToFrequencyMapping.keySet());
		Collections.sort(frequentItems, new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				int diffCount = frequentItemToFrequencyMapping.get(o2) - frequentItemToFrequencyMapping.get(o1);
				return (diffCount != 0) ? diffCount : (o1 - o2);
			}
		});
		
		int minimumTransactionCount = (int) (dataset.getTuples().size() * minSupport);		
		Map<Integer, FPTreeNode> headerTable = new HashMap<Integer, FPTreeNode>();		
		FPTreeNode fpTreeNode = getFPTree(frequentItemToFrequencyMapping, headerTable, dataset);
		recursiveFPGrowth(fpTreeNode, null, headerTable, minimumTransactionCount);
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
	
	/**
	 * Main method to be called in order 
	 * to mine frequent patterns only of size K
	 */
	@Override
	public Map<List<Integer>, Integer> mineKFrequentPattern(int k, double minSupport, Dataset dataset) {
		Map<List<Integer>, Integer> allPatternMapping = mineAllFrequentPattern(minSupport, dataset);
		Map<List<Integer>, Integer> kthPatternMapping = new HashMap<List<Integer>, Integer>();		
		for(Map.Entry<List<Integer>, Integer> entry : allPatternMapping.entrySet()) {
			if(entry.getKey().size() <= k) {
				kthPatternMapping.put(entry.getKey(), entry.getValue());
			}
		}
		return kthPatternMapping;
	}
}
