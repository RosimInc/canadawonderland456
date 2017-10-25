package ets.gti770.spam.classifiers;

import weka.core.Instances;

/**
 * This interface serves as a strategy to classify data following
 *  different algorithms, using a given training set.
 *  
 * @author Jean-Philippe Leclerc
 * @author Jonathan Saindon
 * @author Simon Robert
 * @version 2017-10-25
 */
public interface ISpamClassifierStrategy 
{
	/**
	 * Returns the classification values for the classifier, 1 meaning
	 *  spam and 0 meaning not spam
	 * @param trainData The training set
	 * @param inputData The input data
	 * @return The output results, as a list of 1s and 0s
	 */
	int[] classify(Instances trainData, Instances inputData);
}
