package ets.gti770.spam.utils;

import weka.core.Instance;
import weka.core.Instances;

/**
 * This utility class gives methods to help with machine
 *  learning algorithms.
 *  
 * @author Jean-Philippe Leclerc
 * @author Jonathan Saindon
 * @author Simon Robert
 * @version 2017-10-25
 */
public class LearningUtils {
	
	/**
	 * This utility method returns the entropy of a certain
	 *  probability of spam values.
	 * @param probability Probability of spam results
	 * @return The entropy
	 */
	public static double getEntropy(double probability)
	{
		return -probability * log2(probability) 
				-(1-probability) * log2(1-probability);
	}

	/**
	 * This utility method returns the entropy of a certain
	 *  set of spam and non-spam values.
	 * @param numSpam The number of emails categorized as spam
	 * @param numNonSpam The number of emails not categorized as spam
	 * @return The entropy
	 */
	public static double getEntropy(int numSpam, int numNonSpam)
	{
		if(numSpam * numNonSpam == 0)
			return 0;
		return getEntropy(((double)numSpam)/(numSpam+numNonSpam));
	}
	
	/**
	 * This utility method returns the log base 2 of a
	 *  number.
	 * @param num The number
	 * @return The log base 2 of the number
	 */
	public static double log2(double num)
	{
		return Math.log(num) / Math.log(2);
	}
	
	/**
	 * This utility method computes the best split to perform on a
	 *  continuous attribute in order to maximize the gain.
	 * @param dataSet The dataset to split
	 * @param attributeIndex The attribute to split
	 * @return The set of split informations
	 */
	public static SplitInfo getBestSplit(DataSet dataSet, int attributeIndex)
	{
		Instances instances = dataSet.instances;
		instances.sort(attributeIndex);
		
		// SR Remove
		/*for(Instance i : dataSet.instances)
			System.out.println("  " + i.toString());*/
		
		
		int bestSplit = 1;
		double bestValue = 0.0;
		double bestGain = 0.0;
		
		int cSplit;
		double pValue = Double.MIN_VALUE;
		double cValue;
		double cGain;
		
		// numNonSpams, numSpams
		int[] counts = {0,0};
		
		int pSpamVal; // Previous spam value
		int cSpamVal; // Current spam value
		
		// Get the first value
		Instance instance = instances.get(0);
		pSpamVal = (int)instance.classValue();
		counts[pSpamVal]++;
		
		// Iterate through all the pairs
		int numInstances = instances.size();
		for(cSplit = 1; cSplit < numInstances; cSplit++)
		{
			instance = instances.get(cSplit);
			cSpamVal = (int)instance.classValue();
			cValue = instance.value(attributeIndex);
			
			// If there is a change of trend
			if(pSpamVal != cSpamVal && (cValue - pValue) > 0.001)
			{
				// If there is a better gain, split is kept
				cGain = getGain(dataSet, counts[0], counts[1]);
				
				// SR Remove
				//System.out.println(cGain);
				
				if(cGain > bestGain)
				{
					bestGain = cGain;
					bestSplit = cSplit;
					bestValue = cValue;
				}
			}
			counts[cSpamVal]++;
			
			pSpamVal = cSpamVal;
			pValue = cValue;
		}
			
		// SR Remove
		//System.out.printf("    Best gain + %4.3f\n", bestGain);
		
		return new SplitInfo(attributeIndex, bestSplit, bestValue, bestGain);
	}

	/**
	 * This utility method calculates the gain offered by a split with
	 *  specific number of spam and non-spam values from a dataset.
	 * @param dataSet The dataset to split
	 * @param numNonSpam The number of non-spam values in the subset
	 * @param numSpam The number of spam values in the subset
	 * @return The calculated gain
	 */
	public static double getGain(DataSet dataSet, int numNonSpam, int numSpam)
	{
		double sEntropy = dataSet.entropy;
		int sTotal = dataSet.totalValues;
		
		double lEntropy = getEntropy(numSpam, numNonSpam);
		double rEntropy = getEntropy(dataSet.numSpam - numSpam, 
				dataSet.numNonSpam - numNonSpam);

		int lTotal = numSpam + numNonSpam;
		int rTotal = sTotal - lTotal;
		
		double gain = sEntropy - 
				(lTotal * lEntropy + rTotal * rEntropy) / sTotal;
		
		return gain;
	}
}
