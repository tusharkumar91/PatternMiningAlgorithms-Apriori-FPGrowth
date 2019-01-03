package com.uofr.csc440.project.project1;

/**
 * Enum created to represent the options
 * for running the project
 * 
 * @author tusharkumar
 *
 */
public enum OptionType {

	STRATEGY("strategy"),
	MIN_SUPPORT("support"),
	CONFIDENCE("confidence"),
	RUN_NAIVE_BAYES("runnaivebayes"),
	USE_TEST("usetest");
	
	private String optionName;
	
	private OptionType(String optionName) {
		this.optionName = optionName;
	}

	public String getOptionName() {
		return optionName;
	}
	
	public String toString() {
		return optionName;
	}	
	
	public static OptionType getOptionTypeFromName(String optionName) {
		for(OptionType optionType: OptionType.values()) {
			if(optionType.optionName.equalsIgnoreCase(optionName)) {
				return optionType;
			}
		}
		return null;
	}
	
	public static String getValidOptionNames() {
		StringBuilder str = new StringBuilder();
		for(OptionType optionType: OptionType.values()) {
			str.append(optionType.optionName);
			str.append(",");
			str.append(" ");
		}
		return str.toString();
	}
}
