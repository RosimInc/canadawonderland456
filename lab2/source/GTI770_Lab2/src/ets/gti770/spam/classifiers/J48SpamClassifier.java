package ets.gti770.spam.classifiers;

import weka.core.Instances;

public class J48SpamClassifier implements ISpamClassifierStrategy
{
	@Override
	public int[] classify(Instances data) 
	{
		// TODO To be implemented
		//throw new java.lang.UnsupportedOperationException(
		//		"The C 4.5 classifier is not supported yet.");
		return new int[data.size()];
	}
}
