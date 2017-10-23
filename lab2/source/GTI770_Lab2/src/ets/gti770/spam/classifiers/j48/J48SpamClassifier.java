package ets.gti770.spam.classifiers.j48;

import ets.gti770.spam.classifiers.ISpamClassifierStrategy;
import weka.core.Instances;

public class J48SpamClassifier implements ISpamClassifierStrategy
{
	@Override
	public int[] classify(Instances trainData, Instances inputData) 
	{
		int[] results = new int[inputData.size()];
		
		// TODO J48 - Train data
		
		// TODO J48 - Classify input data
		
		return results;
	}
}
