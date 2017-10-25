package ets.gti770.spam.classifiers.bayes;

import weka.core.Attribute;

/**
 * This class represents a Bayes attribute.
 *  
 * @author Jean-Philippe Leclerc
 * @author Jonathan Saindon
 * @author Simon Robert
 * @version 2017-10-24
 */
class BayesAttribute 
{
	private int truePositive;
	private int trueNegative;
	private int falsePositive;
	private int falseNegative;
	private Attribute wekaAttribute;
	
	/**
	 * This constructor initializes all variables
	 * @param wekaAttribute The Weka attribute
	 */
	public BayesAttribute(Attribute wekaAttribute)
	{
		this.wekaAttribute = wekaAttribute;
		this.truePositive = 0;
		this.trueNegative = 0;
		this.falsePositive = 0;
		this.falseNegative = 0;
	}
	
	/**
	 * This method parses a value to determine if it is
	 *  a false/true positive/negative.
	 * @param value The double value
	 * @param isSpam Whether it is actually spam
	 */
	public void parse(double value, boolean isSpam)
	{
		if (isSpam) 
		{
			if (value > 0.01) 
				truePositive++;
			else
				falseNegative++;
		}
		else 
		{
			if (value > 0.01)
				falsePositive++;
			else
				trueNegative++;
		}
	}
	
	/**
	 * This method returns the probability of this attribute
	 *  being spam.
	 * @return Probability of being spam
	 */
	public double getSpamProbability() 
	{
		double nSpam = truePositive + trueNegative;
		double nNotSpam = falsePositive + trueNegative;
		double total = nSpam + nNotSpam;
		double nWords = truePositive + falsePositive;
		
		return (truePositive * (nSpam / total)) / nWords;
	}

	/**
	 * This getter function returns the Weka attribute
	 * @return The Weka attribute
	 */
	public Attribute getWekaAttribute() 
	{
		return this.wekaAttribute;
	}
}
