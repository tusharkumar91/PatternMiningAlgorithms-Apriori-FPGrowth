package com.uofr.csc440.project.project1.mining.algorithms.apriori;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * CLass to encapsulate the execution of naive bayes
 * algorithm for predicting classes.
 * This will work even when dataset ahs combination of
 * both string and integral attributes.
 * @author tusharkumar
 */
public class NaiveBayes {
	private Map<String, List<List<String>>> classToDiscreteAttributesMapping = new HashMap<String, List<List<String>>>();
	private Map<String, List<List<Integer>>> classToContinuousAttributesMapping = new HashMap<String, List<List<Integer>>>();
	private Map<String, Double> classPriors = new HashMap<String, Double>();
	private Map<String, Map<String, Double>> discreteClassConditionalProbabilities = new HashMap<String, Map<String, Double>>(); 
	private Map<String, List<Double>> continuousClassConditionalValues = new HashMap<String, List<Double>>(); 
	private Map<Integer, List<String>> discreteAttributeListMap = new HashMap<Integer, List<String>>();
	
	/**
	 * Method to build the class Conditional Mappings.
	 * Attributes which exist for each class 
	 * @param tuples
	 * @param classIndex
	 */
	private void buildClassToAttributesMapping(List<List<String>> tuples, int classIndex) {
		System.out.println("Gathering Data to build class Probabilities");
		for(List<String> tuple : tuples) {
			List<String> discreteAttributesList = new ArrayList<String>();
			List<Integer> continuousAttributesList = new ArrayList<Integer>();
			int attributeIndex = 0;
			for(String attribute : tuple) {
				String attributeValue = attribute.trim();
				if(attributeIndex == classIndex) {
					continue;
				}
				try {
					int value = Integer.parseInt(attributeValue);
					continuousAttributesList.add(value);
				}
				catch(NumberFormatException e) {
					if(discreteAttributeListMap.containsKey(attributeIndex)) {
						List<String> attributeValuesPresentAlready = discreteAttributeListMap.get(attributeIndex);
						attributeValuesPresentAlready.add(attributeValue);
					}
					else {						
						List<String> attributeValuesPresentAlready = new ArrayList<String>();
						attributeValuesPresentAlready.add(attributeValue);
						discreteAttributeListMap.put(attributeIndex, attributeValuesPresentAlready);
					}
					discreteAttributesList.add(attributeValue);
				}
				attributeIndex++;
			}
			String classValue = tuple.get(classIndex);
			if(classToContinuousAttributesMapping.containsKey(classValue)) {
				classToContinuousAttributesMapping.get(classValue).add(continuousAttributesList);
			}
			else {
				List<List<Integer>> continuousAttributeForAllRowList = new ArrayList<List<Integer>>();
				continuousAttributeForAllRowList.add(continuousAttributesList);
				classToContinuousAttributesMapping.put(classValue, continuousAttributeForAllRowList);
			}
			
			if(classToDiscreteAttributesMapping.containsKey(classValue)) {
				classToDiscreteAttributesMapping.get(classValue).add(discreteAttributesList);
			}
			else {
				List<List<String>> discreteAttributeForAllRowList = new ArrayList<List<String>>();
				discreteAttributeForAllRowList.add(discreteAttributesList);
				classToDiscreteAttributesMapping.put(classValue, discreteAttributeForAllRowList);
			}
			
		}
	}

	/**
	 * Method to build the class conditional probabilities
	 * which are to be used for prediction
	 * @param classIndex
	 */
	private void buildProbabilityTable(int classIndex) {
		System.out.println("Building Probability Tables");
		double totalTuples = 0;
		for(List<List<String>> classTuples : classToDiscreteAttributesMapping.values()){
			totalTuples += classTuples.size();
		}
		for(String classValue : classToDiscreteAttributesMapping.keySet()){
			classPriors.put(classValue, classToDiscreteAttributesMapping.get(classValue).size()/totalTuples);
		}
		
		//CALCULATE ROW COUNT FOR DISCRETE
		for(String classValue : classToDiscreteAttributesMapping.keySet()){
			Map<String, Double> attributeCount = new HashMap<String, Double>();
			List<List<String>> discreteAttrTuples = classToDiscreteAttributesMapping.get(classValue);
			for(List<String> discreteAttrTuple : discreteAttrTuples) {
				for(String attribute : discreteAttrTuple) {
					Double count = attributeCount.getOrDefault(attribute, 0.0);
					attributeCount.put(attribute, count+1);
				}
			}
			for(String attribute : attributeCount.keySet()) {
				Double count = attributeCount.getOrDefault(attribute, 0.0);
				attributeCount.put(attribute, count/(discreteAttrTuples.size()));
			}
			discreteClassConditionalProbabilities.put(classValue, attributeCount);
		}
		
		//CALCULATE MEAN AND VARIANCE FOR CONTINUOUS
		for(String classValue : classToContinuousAttributesMapping.keySet()){
			List<List<Integer>> continuousAttrTuples = classToContinuousAttributesMapping.get(classValue);
			double[] mean = new double[continuousAttrTuples.get(0).size()];
			
			for(List<Integer> continuousAttrTuple : continuousAttrTuples) {	
				for(int i=0; i<continuousAttrTuple.size(); i++) {
					mean[i] = mean[i] + continuousAttrTuple.get(i);
				}
			}
			for(int i=0; i<mean.length; i++) {
				mean[i] = mean[i]/continuousAttrTuples.size();
			}
			double[] variance = new double[continuousAttrTuples.get(0).size()];
			for(List<Integer> continuousAttrTuple : continuousAttrTuples) {
				for(int i=0; i<continuousAttrTuple.size(); i++) {
					variance[i] = variance[i] + Math.pow((continuousAttrTuple.get(i) - mean[i]), 2);
				}
			}
			
			for(int i=0; i<variance.length; i++) {
				variance[i] = variance[i]/continuousAttrTuples.size();
				
			}
			List<Double> meanAndVariance = new ArrayList<Double>();
			for(int i=0; i<mean.length; i++) {
				meanAndVariance.add(mean[i]);
				meanAndVariance.add(variance[i]);
			}
			continuousClassConditionalValues.put(classValue, meanAndVariance);			
		}
	}
	
	/**
	 * Method to fetch data for which it assumes to be a list 
	 * of string
	 * @param filepath
	 * @return
	 */
	public List<List<String>> getData(String filepath){
		BufferedReader reader;
		List<List<String>> tuples = new ArrayList<List<String>>();		
		try {
			reader = new BufferedReader(new FileReader(
					filepath));
			String line = reader.readLine();
			while (line != null) {
				List<String> row = getRowFromLine(line);
				if(row != null) {
					tuples.add(row);
				}
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return tuples;
	}
	
	/**
	 * Method to predict the test data using the class
	 * conditional probabilities built.
	 * @param tuples
	 * @param classIndex
	 * @return
	 */
	private List<String> predict(List<List<String>> tuples, int classIndex){
		System.out.println("Gathering predictions based on probabilities");
		List<String> classPrediction = new ArrayList<String>();
		for(List<String> tuple : tuples) {
			List<String> discreteAttributesList = new ArrayList<String>();
			List<Integer> continuousAttributesList = new ArrayList<Integer>();
			int attributeIndex = 0;
			for(String attribute : tuple) {
				String attributeValue = attribute.trim();
				if(attributeIndex == classIndex) {
					continue;
				}
				try {
					int value = Integer.parseInt(attributeValue);
					continuousAttributesList.add(value);
				}
				catch(NumberFormatException e) {
					discreteAttributesList.add(attributeValue);
				}
				attributeIndex++;
			}
			
			String probableClass = "";
			double probability = -Double.MAX_VALUE;
			for(String classValue : discreteClassConditionalProbabilities.keySet()) {
				Map<String, Double> discreteClassValueConditionalProbabilities = discreteClassConditionalProbabilities.get(classValue);
				List<Double> continuousClassValueConditionalMeanAndVariance = continuousClassConditionalValues.get(classValue);
				double classProbability = 1;
				for(String discreteAttribute : discreteAttributesList) {
					if(discreteClassValueConditionalProbabilities.containsKey(discreteAttribute)) {
						classProbability = classProbability * (discreteClassValueConditionalProbabilities.get(discreteAttribute));
					}
					else {
						for(List<String> discreteValues : discreteAttributeListMap.values()) {
							if(discreteValues.contains(discreteAttribute)) {
								double prob = 1.0 / (2*discreteValues.size());
								classProbability = classProbability * prob;
								break;
							}							
						}
					}
				}
				for(int i=0; i< continuousAttributesList.size(); i=i+1) {
					int continuousAttribute = continuousAttributesList.get(i);
					double gaussianProbability = getGaussianProbability(continuousAttribute, 
							continuousClassValueConditionalMeanAndVariance.get(2*i), 
							continuousClassValueConditionalMeanAndVariance.get((2*i)+1));
					classProbability = classProbability * gaussianProbability;
				}	
				if(classProbability > probability) {
					probability = classProbability;
					probableClass = classValue;
				}
			}
			classPrediction.add(probableClass);			
		}
		return classPrediction;
	}
	
	/**
	 * Show the confusion matrix and all the different metrics 
	 * @param predictions
	 * @param data
	 * @param classIndex
	 */
	private void getPerformanceMetrics(List<String> predictions, List<List<String>> data, int classIndex) {
		double TP = 0;
		double TN = 0;
		double FP = 0;
		double FN = 0;
		for(int i=0; i<predictions.size(); i++) {
			String prediction = predictions.get(i).trim();			
			List<String> tuple = data.get(i);
			if(prediction.equalsIgnoreCase("<=50K")){
				if(tuple.get(classIndex).contains(prediction)) {
					TN++;
				}
				else {
					FN++;
				}
			}
			else if(prediction.equalsIgnoreCase(">50K")) {
				if(tuple.get(classIndex).contains(prediction)) {
					TP++;
				}
				else {
					FP++;
				}
			}
		}
		System.out.println("\n\n===================================");
		System.out.println("Performance metrics for Naive Bayes");
		System.out.println("===================================");
		System.out.println("Treating <= 50K as negative class and >50K as positive class");
		System.out.println("True Positive : " + TP);
		System.out.println("False Positive : " + FP);
		System.out.println("True Negative : " + TN);
		System.out.println("False Negative : " + FN);
		System.out.println("---------------------------");
		double accuracy = (((double)(TP+TN))/(TP+FP+TN+FN));
		double precision = (((double)(TP))/(TP+FP));
		double recall = (((double)(TP))/(TP+FN));
		double neg_pv = (((double)(TN))/(TN+FN));
		double fscore = (((double)(2*precision*recall))/(precision+recall));
		System.out.println("Precision : " + precision);
		System.out.println("Recall : " + recall);
		System.out.println("F1 Score : " + fscore);
		System.out.println("Negative Predictive Value : " + neg_pv);
		System.out.println("Accuracy on test data using Naive Bayes: " + accuracy);
	}
	
	/**
	 * Utility method to calculate gaussian probability values 
	 * @param continuousAttribute
	 * @param mean
	 * @param variance
	 * @return
	 */
	private double getGaussianProbability(int continuousAttribute, Double mean, Double variance) {
		double xMinusMean = Math.pow((continuousAttribute - mean), 2);
		double exponentValue = - (xMinusMean / (2* variance));
		double exponent = Math.exp(exponentValue);
		return exponent/(Math.pow(variance*2*Math.PI, 0.5));		
	}

	/**
	 * Method to extract each row.
	 * We ignore all unknown values
	 * @param line
	 * @return
	 */
	private List<String> getRowFromLine(String line) {
		line = line.trim();	
		String[] values = line.split(",");
		if(!(values.length > 1)) {
			return null;
		}
		List<String> attributeValues = new ArrayList<String>();
		for(int i=0; i<values.length; i++) {
			if(i == 2) {
				continue;
			}			
			if(values[i].contains("?")) {
				attributeValues.add(i + "_UNKNOWN");
			}
			else{
				attributeValues.add(values[i]);
			}
		}
		return attributeValues;
	}
	
	public void runNaiveBayes(String trainDataPath, String testDataPath) {
		System.out.println("\n\n===================================");
		System.out.println("Running Naive Bayes Algorithm");
		System.out.println("===================================");
		List<List<String>> data = getData(trainDataPath);		
		int classIndex = data.get(0).size()-1;
		buildClassToAttributesMapping(data, classIndex);
		buildProbabilityTable(classIndex);
		data = getData(testDataPath);
		List<String> predictions = predict(data, classIndex);
		getPerformanceMetrics(predictions, data, classIndex);
	}
	
	public static void main(String[] args) {
		NaiveBayes nb = new NaiveBayes();
		nb.runNaiveBayes("src/com/uofr/csc440/project/project1/data/adult.data", "src/com/uofr/csc440/project/project1/data/adult.test");
	}
}
