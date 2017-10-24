package ets.gti770.spam.classifiers.j48;

import ets.gti770.spam.classifiers.ISpamClassifierStrategy;
import weka.core.Instances;

public class J48SpamClassifier implements ISpamClassifierStrategy
{
	@Override
	public int[] classify(Instances trainData, Instances inputData) 
	{
		int[] results = new int[inputData.size()];
		
		// SR J48 - Train data
		
		// SR J48 - Classify input data
		
		// SR J48 - Make sure all new classes are package
		
		return results;
	}
}
