package com.uofr.csc440.project.project1.mining.algorithms.apriori;

import com.uofr.csc440.project.project1.FrequentItemsetMiningStrategy;
import com.uofr.csc440.project.project1.mining.algorithms.fpgrowth.FPGrowth;

/**
 * Factory class for getting the appropriate mining strategy object
 * given the argument as its strategy name.
 * @author tusharkumar
 *
 */
public final class MiningStrategyFactory {
	public static FrequentItemsetMiningStrategy getStrategy(MiningStrategy strategy) {
		if(MiningStrategy.APRIORI.equals(strategy)) {
			return new Apriori();
		}
		else if(MiningStrategy.APRIORI_IMPROVED.equals(strategy)) {
			return new AprioriImproved();
		}
		else if(MiningStrategy.FP_GROWTH.equals(strategy)) {
			return new FPGrowth();
		}
		else {
			return null;
		}
	}
}
