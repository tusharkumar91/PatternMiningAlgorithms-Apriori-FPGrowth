package com.uofr.csc440.project.project1.mining.algorithms.apriori;

/**
 * ENUM for strategy that can be used for Mining
 * @author tusharkumar
 *
 */
public enum MiningStrategy {
	APRIORI("Apriori"),
	APRIORI_IMPROVED("Apriori-Improved"),
	FP_GROWTH("FPGrowth");
	
	private String strategyName;
	
	private MiningStrategy(String strategy) {
		this.strategyName = strategy;
	}
	
	public String getStrategyName() {
		return strategyName;
	}
	
	public static MiningStrategy getMiningStrategy(String strategy) {
		for(MiningStrategy strategyType : MiningStrategy.values()) {
			if(strategyType.getStrategyName().equalsIgnoreCase(strategy)) {
				return strategyType;
			}
		}
		return null;
	}
}
