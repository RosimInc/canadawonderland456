package ets.gti770.spam.classifiers;

import weka.core.Instances;

public interface ISpamClassifierStrategy 
{
	/**
	 * Returns the classification values for the classifier, 1 meaning
	 *  spam and 0 meaning not spam
	 * @param data The input data
	 * @return The output results, as a list of 1s and 0s
	 */
	int[] classify(Instances data);
}
