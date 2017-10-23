package ets.gti770.spam.classifiers.bayes;

import ets.gti770.spam.classifiers.ISpamClassifierStrategy;
import weka.core.Instances;

public class BayesSpamClassifier implements ISpamClassifierStrategy 
{
	@Override
	public int[] classify(Instances trainData, Instances inputData) 
	{
		int[] results = new int[inputData.size()];
		
		// TODO Bayes - Train data
		
		// TODO Bayes - Classify input data
		
		return results;
	}
}
