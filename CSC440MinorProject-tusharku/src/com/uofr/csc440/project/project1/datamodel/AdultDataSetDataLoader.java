package com.uofr.csc440.project.project1.datamodel;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;


/**
 * Class to load the data from data files.
 * Note that this class is specfically to handle
 * data for Adult dataset downloaded from 
 * http://archive.ics.uci.edu/ml/datasets/Adult
 * @author tusharkumar
 */
public class AdultDataSetDataLoader implements DataLoader {
	
	/**
	 * Helper method to get a row/tuple in the form 
	 * of string and convert it into integral values based
	 * on knowledge of dataset
	 * @param line
	 * @param attributes
	 * @return
	 */
	private Row getRowFromLine(String line, List<Integer> attributes) {
		line = line.trim();	
		Row row = null;
		String[] values = line.split(",");
		if(!(values.length > 1)) {
			return null;
		}
		List<Integer> attributeValues = new ArrayList<Integer>();
		int j=0;
		for(int i=0; i<values.length; i++) {
			/*
			 * Ignoring the finalWeight Attribute because
			 *  we will not be using the weight
			 *  and it just represent a data attribute.
			 *  We could choose to weight the dataset rows by 
			 *  that number.
			 */
			if(i == 2) {
				continue;
			}
			AdultDataSetItem adultDataSetItem = AdultDataSetItem.getAttributeItem(values[i], j+1);
			if(adultDataSetItem == null) {
				attributeValues.add(-1);
			}
			attributeValues.add(adultDataSetItem.getValue());
			j++;
		}
		row = new Row(attributeValues);
		return row;
	}
	
	/**
	 * Helper method to convert the list of attributes 
	 * mentioned in the form of string in Adult Data set
	 * to an integral value.
	 * @return
	 */
	private List<Integer> getAttributes() {
		AdultDataSetItem[] attributes = AdultDataSetItem.values();			
		Arrays.sort(attributes, new Comparator<AdultDataSetItem>() {
			@Override
			public int compare(AdultDataSetItem o1, AdultDataSetItem o2) {
				return o1.getColumnIndex() - o2.getColumnIndex();
			}
		});
		List<Integer> attributeValues = new ArrayList<Integer>();
		for(AdultDataSetItem item : attributes) {
			attributeValues.add(item.getValue());
		}
		return attributeValues;
	}
	
	/**
	 * Overridden method that actually does the heavy lifting 
	 * of loading the data specified at the given file path
	 * into the memory and converting it into a format
	 * easy for handling and itemset search.
	 */
	@Override
	public Dataset loadData(String filepath) {
		Dataset dataset = new Dataset();
		List<Integer> attributes = getAttributes();
		List<Row> tuples = new ArrayList<Row>();
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(
					filepath));
			String line = reader.readLine();
			while (line != null) {
				Row row = getRowFromLine(line, attributes);
				if(row != null) {
					for(int i=0; i<1; i++) {
						//Can change this structure to simulate bigger dataset.
						//Will Duplicate each row max(i) times
						tuples.add(row);
					}
				}
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		dataset.setTuples(tuples);
		dataset.setAttributes(attributes);
		return dataset;
	}		
}
	