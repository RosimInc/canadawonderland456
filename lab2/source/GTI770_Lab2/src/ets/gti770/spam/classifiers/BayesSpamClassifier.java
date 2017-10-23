package ets.gti770.spam.classifiers;

import weka.core.Instances;

public class BayesSpamClassifier implements ISpamClassifierStrategy 
{
	@Override
	public int[] classify(Instances data) 
	{
		// TODO To be implemented
		//throw new java.lang.UnsupportedOperationException(
		//		"The Bayes classifier is not supported yet.");
		return new int[data.size()];
	}
}
