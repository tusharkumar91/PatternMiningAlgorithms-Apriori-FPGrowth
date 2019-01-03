package com.uofr.csc440.project.project1.datamodel;

import java.util.List;

/**
 * Dataset object which can contain row.tuples
 * of list integer. Each integral value represents
 * a specific column value of that row.
 * @author tusharkumar
 *
 */
public class Dataset {
	private List<Row> tuples;
	private List<Integer> attributes;

	public List<Row> getTuples(){
		return tuples;
	}

	public List<Integer> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<Integer> attributes) {
		this.attributes = attributes;
	}

	public void setTuples(List<Row> tuples) {
		this.tuples = tuples;
	}

}
