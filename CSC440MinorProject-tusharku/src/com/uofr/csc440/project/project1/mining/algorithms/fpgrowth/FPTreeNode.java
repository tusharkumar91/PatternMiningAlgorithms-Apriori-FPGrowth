package com.uofr.csc440.project.project1.mining.algorithms.fpgrowth;

import java.util.Map;

/**
 * Class to encapsulate a node in FPTree
 * Each node stores its value, frequency and children 
 * @author tusharkumar
 *
 */
public class FPTreeNode {
	int itemValue;
	int itemFrequency;
	boolean isRootNode;
	
	//Parent of current node
	private FPTreeNode parentNode;
	
	/*
	 * Next sibling of node, basically 
	 * connects header node to all nodes of same value
	 */	
	private FPTreeNode nextSiblingNode; 
	
	/*
	 * Map of value of child to childNode
	 */
	private Map<Integer, FPTreeNode> childNodes;
	
	public FPTreeNode(int itemValue, int itemFrequency, FPTreeNode parentNode) {
		super();
		this.itemValue = itemValue;
		this.itemFrequency = itemFrequency;
		this.parentNode = parentNode;
		this.nextSiblingNode = null;
		this.childNodes = null;
	}
	
	public int getItemValue() {
		return itemValue;
	}
	public void setItemValue(int itemValue) {
		this.itemValue = itemValue;
	}
	public int getItemFrequency() {
		return itemFrequency;
	}
	public void setItemFrequency(int itemFrequency) {
		this.itemFrequency = itemFrequency;
	}
	public FPTreeNode getparentNode() {
		return parentNode;
	}
	public void setparentNode(FPTreeNode parentNode) {
		this.parentNode = parentNode;
	}
	public FPTreeNode getNextSiblingNode() {
		return nextSiblingNode;
	}
	public void setNextSiblingNode(FPTreeNode nextSiblingNode) {
		this.nextSiblingNode = nextSiblingNode;
	}
	public Map<Integer, FPTreeNode> getChildNodes() {
		return childNodes;
	}
	public void setChildNodes(Map<Integer, FPTreeNode> childNodes) {
		this.childNodes = childNodes;
	}
	
	public boolean isRootNode() {
		return isRootNode;
	}
	
	public void setRootNode(boolean isRootNode) {
		this.isRootNode = isRootNode;
	}
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("value = " + this.itemValue);
		str.append(" ; ");
		str.append("frequency = " + this.itemFrequency);
		return str.toString();
	}	
	
}
