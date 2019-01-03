package com.uofr.csc440.project.project1.mining.algorithms.apriori;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.uofr.csc440.project.project1.datamodel.Dataset;
import com.uofr.csc440.project.project1.datamodel.Row;

/**
 * Class to encapsulate improvements in Apriori
 * algorithm. Two improvements are included
 * 1. Reduction in number of transaction scans
 * 2. While scanning the rows for checking support 
 * 	of candidate items do it in parallel. Its loosely based on
 * the idea of partitioning in textbook.
 * @author tusharkumar
 *
 */
public class AprioriImproved extends Apriori{

	/**
	 * Overridden method to filter out the candidates which we generate 
	 * by actually scanning the database and finding that their
	 * support is meeting the minimum criteria.
	 * @param minimumTransactionCount
	 * @param kthItemSetCandidatesAfterPruning
	 * @param dataset
	 * @return
	 */
	@Override
	protected List<List<Integer>> getCandidatesPassingSupport(int minimumTransactionCount,
			List<List<Integer>> kthItemSetCandidatesAfterPruning, Dataset dataset) {
		
		List<Row> tuples = dataset.getTuples();	
		//Divide the entire dataset into five parts.
		int partitionSize = tuples.size()/5;
		List<List<Row>> partitionedRows = new ArrayList<List<Row>>();
		for (int j = 0; j < tuples.size(); j += partitionSize) {
			partitionedRows.add(new ArrayList<Row>((tuples.subList(j,
					Math.min(j + partitionSize, tuples.size())))));		
		}			
		
		/**
		 * Have concurrent map to ensure that all
		 * updates to support of an item set
		 * are done in a thread safe manner.
		 */
		Map<Integer, Integer> itemSetToFrequencyMapping = new ConcurrentHashMap<Integer, Integer>();
		for(int j = 0; j < kthItemSetCandidatesAfterPruning.size(); j++){
			itemSetToFrequencyMapping.put(j, 0);
		}
		List<List<Integer>> kthItemSet = new ArrayList<List<Integer>>();

		/**
		 * Spawn a thread so that each thread will handle each partition
		 * Giving it a value of 5 should be fine as that would not be causing
		 * too much load and not end up doing more harm than good.
		 * Not using fixed thread pool because I am anyways creating smaller 
		 * number of threads so we can afford to start a new one rather 
		 * than waiting for existing ones to be idle.
		 */
		ExecutorService executorService = Executors.newCachedThreadPool();
		List<Future<?>> tasks = new ArrayList<Future<?>>();
		for(List<Row> rows : partitionedRows) {
			tasks.add(executorService.submit(new Runnable() {
				/**
				 * Runnable to extract the support for each candidate
				 * in each partition in parallel and update the count
				 * in the concurrent map created above.
				 */
				@Override
				public void run() {
					findSupportForEachCandidateInEachParition(kthItemSetCandidatesAfterPruning,
							itemSetToFrequencyMapping, rows);				
				}								
			}));			
		}			
		/**
		 * Block for all five partition scans to be complete
		 */
		for(Future<?> task : tasks) {
			try {
				task.get(1, TimeUnit.MINUTES);
			} catch (InterruptedException | ExecutionException | TimeoutException e) {
				/**
				 * One of the tasks could not be completed within the specified
				 * time limits of 60 seconds.
				 */
				System.err.println("One of the future tasks could not be completed "
						+ "within specified timeout");
				e.printStackTrace();
			}
		}
		executorService.shutdown();
		for(int j = 0; j < kthItemSetCandidatesAfterPruning.size(); j++){
			if(itemSetToFrequencyMapping.containsKey(j) && (itemSetToFrequencyMapping.get(j).intValue() >= minimumTransactionCount)) {
				kthItemSet.add(kthItemSetCandidatesAfterPruning.get(j));
				patternMapping.put(kthItemSetCandidatesAfterPruning.get(j), itemSetToFrequencyMapping.get(j));
			}
		}
		
		List<Row> updatedTuplesList = new ArrayList<Row>();
		for(List<Row> rows : partitionedRows) {
			updatedTuplesList.addAll(rows);
		}
		return kthItemSet;
	}
	
	/**
	 * @param kthItemSetCandidatesAfterPruning
	 * @param itemSetToFrequencyMapping
	 * @param rows
	 */
	private void findSupportForEachCandidateInEachParition(
			List<List<Integer>> kthItemSetCandidatesAfterPruning,
			Map<Integer, Integer> itemSetToFrequencyMapping, List<Row> rows) {
		
		for(int i=0; i<rows.size();i++) {
			Row tuple = rows.get(i);
			totalTransactionScans++;
			boolean tupleContainsFrequentItem = false;
			for(int j = 0; j < kthItemSetCandidatesAfterPruning.size(); j++){
				List<Integer> candidateItemSet = kthItemSetCandidatesAfterPruning.get(j);
				List<Integer> attributeValues = tuple.getAttributeValues();
				if(attributeValues.containsAll(candidateItemSet)){
					itemSetToFrequencyMapping.compute(j, (key, value) -> value == null ? 1 : value + 1);
					tupleContainsFrequentItem = true;
				}			
			}
			/**
			 * If any row does not contain a frequent item
			 * it will not contain any future frequent item
			 * also. Lets remove from our dataset.
			 */
			if(!tupleContainsFrequentItem) {
				rows.remove(i);
				i=i-1;
			}
		}
	}
}
