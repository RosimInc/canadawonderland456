package ets.gti770.spam.classifiers.knn;

import ets.gti770.spam.classifiers.ISpamClassifierStrategy;
import weka.core.Instances;

public class KNNSpamClassifier implements ISpamClassifierStrategy 
{
	@Override
	public int[] classify(Instances trainData, Instances inputData)
	{
		throw new java.lang.UnsupportedOperationException(
				"The KNN classifier is not supported yet.");
	}
}
