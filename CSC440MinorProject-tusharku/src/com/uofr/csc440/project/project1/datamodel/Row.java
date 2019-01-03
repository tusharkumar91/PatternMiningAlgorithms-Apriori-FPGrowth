package com.uofr.csc440.project.project1.datamodel;

import java.util.List;

/**
 * Class representing a Row in any dataset
 * @author tusharkumar
 *
 */
public class Row {
	private List<Integer> attributeValues;
	
	public Row(List<Integer> attributeValues) {
		this.attributeValues = attributeValues;
	}
	
	public List<Integer> getAttributeValues() {
		return attributeValues;
	}
	
	public void setAttributeValues(List<Integer> attributeValues) {
		this.attributeValues = attributeValues;
	}
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		if(attributeValues != null) {
			for(int i=0; i<attributeValues.size(); i++) {
				if(i != 0) {
					str.append(", ");
				}
				if(attributeValues.get(i) == null) {
					System.out.println("null");
				}
				str.append(attributeValues.get(i));
			}
		}
		return str.toString();
	}
}
