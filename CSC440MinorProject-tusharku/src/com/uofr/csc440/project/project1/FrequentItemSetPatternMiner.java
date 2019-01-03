package com.uofr.csc440.project.project1;

import java.util.List;
import java.util.Map;

import com.uofr.csc440.project.project1.datamodel.AdultDataSetDataLoader;
import com.uofr.csc440.project.project1.datamodel.AdultDataSetItem;
import com.uofr.csc440.project.project1.datamodel.DataLoader;
import com.uofr.csc440.project.project1.datamodel.Dataset;
import com.uofr.csc440.project.project1.datamodel.Row;
import com.uofr.csc440.project.project1.datamodel.formatter.AdultDataItemSetFormatter;
import com.uofr.csc440.project.project1.datamodel.formatter.ItemsetFormatter;
import com.uofr.csc440.project.project1.mining.algorithms.apriori.MiningStrategy;
import com.uofr.csc440.project.project1.mining.algorithms.apriori.MiningStrategyFactory;
import com.uofr.csc440.project.project1.mining.algorithms.apriori.NaiveBayes;


/**
 * Main Class to actually run the different requirements 
 * of CSC440 Project. 
 * This will use arguments to know what strategy to use to 
 * generate item sets. 
 * Will also need the minimum relative support and confidence for generating
 * association rules and doing prediction in case that is what the
 * client requires. 
 * By Default it will just run the FPGrowth strategy and print the item sets.
 * @author tusharkumar
 *
 */
public class FrequentItemSetPatternMiner {

	private static final String ADULT_TRAIN_DATA_PATH = "src/com/uofr/csc440/project/project1/data/adult.data";
	private static final String ADULT_TEST_DATA_PATH = "src/com/uofr/csc440/project/project1/data/adult.test";
	
	/**
	 * MAIN method to do all the workflow related to the project.
	 * Will accept the following parameters as input
	 * minsupport - type(double)
	 * strategy -  type(Apriori/FPGrowth/Apriori-Improved)
	 * confidence - type(double)
	 * runnaivebayes - type(boolean)
	 * @param args
	 */
	public void runMining(double minSupport, double confidence, boolean runNaiveBayes, MiningStrategy miningStrategy, boolean useTest) {				
		// LOAD DATA AND PRE-PROCESS
		DataLoader datasetLoader = new AdultDataSetDataLoader();
		Dataset dataset = datasetLoader.loadData(ADULT_TRAIN_DATA_PATH);
		if(useTest) {
			dataset = datasetLoader.loadData(ADULT_TEST_DATA_PATH);
		}
		System.out.println("\nTotal Transactions : " + dataset.getTuples().size());

		// RUN PATTERN MINING ALGORITHM
		long startTime = System.currentTimeMillis();
		FrequentItemsetMiningStrategy strategy = MiningStrategyFactory.getStrategy(miningStrategy);
		Map<List<Integer>, Integer> frequentItemSets = strategy.mineAllFrequentPattern(minSupport, dataset);
		
		System.out.println("\nTotal Transactions Scanned :" + strategy.getTotalTransactionScans());
		System.out.println("\nTime taken (in seconds) to find frequent itemsets with support of " + 
							minSupport + " = " + ((float)(System.currentTimeMillis() - startTime))/1000 + " seconds");
		
		//FORMAT AND PRINT ITEMSETS
		System.out.println("\nItemsets generated using " + miningStrategy.getStrategyName() + "\n");
		ItemsetFormatter formatter = new AdultDataItemSetFormatter();
		formatter.setTotalTransactions(dataset.getTuples().size());
		formatter.formatAndPrintItemSets(frequentItemSets);
		
		//GET ASSOCIATION RULES
		if(confidence > 0.0) {
			Map<List<Integer>, Double> confForLessthan50K = strategy.getConfidenceRules(AdultDataSetItem.SALARY_LESS_50K.getValue(), confidence);
			System.out.println("\n\n=============================");
			System.out.println("Sample Association Rules Extracted for " + AdultDataSetItem.SALARY_LESS_50K.getItemName());
			printSampleAssociationRule(confForLessthan50K, AdultDataSetItem.SALARY_LESS_50K);
			System.out.println("\n=============================\n");
			Map<List<Integer>, Double> confForGreaterthan50K = strategy.getConfidenceRules(AdultDataSetItem.SALARY_MORE_50K.getValue(), confidence);
			System.out.println("Sample Association Rules Extracted for " + AdultDataSetItem.SALARY_MORE_50K.getItemName());
			printSampleAssociationRule(confForGreaterthan50K, AdultDataSetItem.SALARY_MORE_50K);
			System.out.println("\n=============================\n");
		
			//PREDICT TEST DATA USING ASSOCIATION RULES
			predictOnTestDataUsingAssociationRules(datasetLoader, confForLessthan50K, confForGreaterthan50K);
		}
		
		//RUN NAIVE BAYES ALGORITHM
		if(runNaiveBayes) {
			NaiveBayes naiveBayes = new NaiveBayes();
			naiveBayes.runNaiveBayes(ADULT_TRAIN_DATA_PATH, ADULT_TEST_DATA_PATH);
		}
	}

	private void printSampleAssociationRule(Map<List<Integer>, Double> confMapping,
			AdultDataSetItem salary) {
		int maxSamples = 2;
		System.out.println("\n------"+maxSamples+" Sample Association Rules------\n");
		for(Map.Entry<List<Integer>, Double> entry : confMapping.entrySet()) {
			if(maxSamples <= 0) {
				break;
			}
			StringBuilder associationRule = new StringBuilder();
			associationRule.append("{ ");
			for(int itemIndex = 0; itemIndex < entry.getKey().size(); itemIndex++) {
				AdultDataSetItem formattedItem = AdultDataSetItem.getItemFromValue(entry.getKey().get(itemIndex));
				associationRule.append(formattedItem);
				if(itemIndex != entry.getKey().size()-1) {
					associationRule.append(", ");
				}
				else {
					associationRule.append(" } ");
				}
			}
			associationRule.append("=> ");			
			associationRule.append(salary.getItemName());
			associationRule.append("\nConfidence = " + entry.getValue().floatValue());
			System.out.println(associationRule.toString());
			System.out.println("\n");
			maxSamples--;
		}		
	}

	/**
	 * Method to predict on test data for adult data set using 
	 * association rules generated by the provided strategy from user.
	 * @param datasetLoader
	 * @param confForLessthan50K
	 * @param confForGreaterthan50K
	 */
	private void predictOnTestDataUsingAssociationRules(DataLoader datasetLoader,
			Map<List<Integer>, Double> confForLessthan50K, Map<List<Integer>, Double> confForGreaterthan50K) {
		System.out.println("Running Predictions using the Association Rules Generated on Test data\n");
		System.out.println("---------------------------------------------------------");
		Dataset dataset = datasetLoader.loadData(ADULT_TEST_DATA_PATH);
		if(confForLessthan50K == null && confForGreaterthan50K == null) {
			return;
		}
		int TP = 0;
		int FP = 0;
		int TN = 0;
		int FN = 0;		
		for(Row row : dataset.getTuples()) {
			List<Integer> attributes = row.getAttributeValues();
			double positiveScore = 0;
			double negativeScore = 0;
			if(confForLessthan50K != null) {
				for(List<Integer> rulesNegative : confForLessthan50K.keySet()) {
					if(attributes.containsAll(rulesNegative)) {
						negativeScore = Math.max(negativeScore, confForLessthan50K.get(rulesNegative));
					}
				}
			}			
			if(confForGreaterthan50K != null) {
				for(List<Integer> rulesPositive : confForGreaterthan50K.keySet()) {
					if(attributes.containsAll(rulesPositive)) {
						positiveScore = Math.max(positiveScore, confForGreaterthan50K.get(rulesPositive));
					}
				}
			}
			if(negativeScore == positiveScore && positiveScore == 0.0) {
				continue;
			}			
			if(negativeScore > positiveScore) {
				//pred = less than 50k
				if(row.getAttributeValues().get(AdultDataSetItem.SALARY_LESS_50K.getColumnIndex()-1) == AdultDataSetItem.SALARY_LESS_50K.getValue()) {
					TN++;
				}
				else{
					FN++;
				}
			}
			else if(positiveScore > negativeScore){				
				//pred = more than 50k
				if(row.getAttributeValues().get(AdultDataSetItem.SALARY_MORE_50K.getColumnIndex()-1) == AdultDataSetItem.SALARY_MORE_50K.getValue()) {
					TP++;
				}
				else{
					FP++;
				}
			}
		}				
		double accuracy = (((double)(TP+TN))/(TP+FP+TN+FN));
		double precision = (((double)(TP))/(TP+FP));
		double recall = (((double)(TP))/(TP+FN));
		double neg_pv = (((double)(TN))/(TN+FN));
		double fscore = (((double)(2*precision*recall))/(precision+recall));
		System.out.println("Precision : " + precision);
		System.out.println("Recall : " + recall);
		System.out.println("F1 Score : " + fscore);
		System.out.println("Negative Predictive Value : " + neg_pv);
		System.out.println("Accuracy of Predictions on test data using association rules: " + accuracy);
	}
	
	/**
	 * Main method to initiate the mining
	 */
	public static void main(String[] args) {
		MiningStrategy miningStrategy = MiningStrategy.APRIORI;
		double minSupport = 0.23;
		double confidence = 0.0;
		boolean runNaiveBayes = false;
		boolean useTest = false;
		
		//GET ARGUMENTS
		if(args.length > 0) {
			for(int i=0; i<args.length; i+=2) {
				if(args[i].charAt(0) != '-') {
					throw new IllegalArgumentException("argument name not prefixed with -");
				}
				if(args[i].length() <= 1) {
					throw new IllegalArgumentException("argument name must be of more than 1 character");
				}
				OptionType argumentName = OptionType.getOptionTypeFromName(args[i].substring(1));
				if(argumentName == null) {
					throw new IllegalArgumentException("argument name must be amongst the following : " + OptionType.getValidOptionNames() + 
					" but was " + args[i].substring(1));
				}
				
				if(i+1 >= args.length) {
					throw new IllegalArgumentException("No value given for option " + argumentName.getOptionName());
				}
				String argumentValue = args[i+1];
				switch(argumentName) {
				case STRATEGY:
					if("apriori".equalsIgnoreCase(argumentValue.toLowerCase())) {
						miningStrategy = MiningStrategy.APRIORI;
					}
					if("apriori-improved".equalsIgnoreCase(argumentValue.toLowerCase())) {
						miningStrategy = MiningStrategy.APRIORI_IMPROVED;
					}
					if("fpgrowth".equalsIgnoreCase(argumentValue.toLowerCase())) {
						miningStrategy = MiningStrategy.FP_GROWTH;
					}					
					break;
				case MIN_SUPPORT:
					try{
						minSupport = Double.parseDouble(argumentValue);
					}
					catch(NumberFormatException e) {
						throw new IllegalArgumentException("Unable to get the float value for Support");
					}
					break;
				case CONFIDENCE:
					try{
						confidence = Double.parseDouble(argumentValue);
					}
					catch(NumberFormatException e) {
						throw new IllegalArgumentException("Unable to get the float value for Confidence");
					}
					break;
				case RUN_NAIVE_BAYES :
					runNaiveBayes = Boolean.parseBoolean(argumentValue);
					break;
				case USE_TEST : 
					useTest = Boolean.parseBoolean(argumentValue);
				}								
			}			
		}
		FrequentItemSetPatternMiner frequentPatternMiner = new FrequentItemSetPatternMiner();
		frequentPatternMiner.runMining(minSupport, confidence, runNaiveBayes, miningStrategy, useTest);		
	}
}
