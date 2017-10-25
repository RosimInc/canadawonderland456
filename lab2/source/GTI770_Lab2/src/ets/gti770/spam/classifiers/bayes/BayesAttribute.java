package ets.gti770.spam.classifiers.bayes;

import weka.core.Attribute;

/**
 * This class trains and classifies data following the Bayes
 *  algorithm.
 * @author Jean-Philippe Leclerc
 * @author Jonathan Saindon
 * @author Simon Robert
 * @version 2017-10-24
 */
class BayesAttribute {
	private int truePositive;
	private int trueNegative;
	private int falsePositive;
	private int falseNegative;
	private Attribute wekaAttribute;
	
	public BayesAttribute(Attribute wekaAttribute) {
		this.wekaAttribute = wekaAttribute;
		this.truePositive = 0;
		this.trueNegative = 0;
		this.falsePositive = 0;
		this.falseNegative = 0;
	}
	
	public void parse(double value, boolean isSpam) {
		if (isSpam) {
			if (value > 0.0) {
				truePositive++;
			} else {
				falseNegative++;
			}
		}
		else {
			if (value > 0.0) {
				falsePositive++;
			} else {
				trueNegative++;
			}
		}
	}
	
	public double getSpamProbability() {
		double nSpam = truePositive + trueNegative;
		double nNotSpam = falsePositive + trueNegative;
		double total = nSpam + nNotSpam;
		double nWords = truePositive + falsePositive;
		return (truePositive * (nSpam / total)) / nWords;
	}

	public Attribute getWekaAttribute() {
		return this.wekaAttribute;
	}
}
