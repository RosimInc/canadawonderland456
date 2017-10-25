package ets.gti770.spam.utils;

/**
 * This utility class gives methods to help with machine
 *  learning algorithms.
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
	
	/*public static void main(String[] args) {
		System.out.println("Testing utilities");
		System.out.println(log2(256));
		System.out.println(getEntropy(5,9));
	}*/
}
