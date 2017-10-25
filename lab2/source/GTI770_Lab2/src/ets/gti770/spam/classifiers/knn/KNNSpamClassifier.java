package ets.gti770.spam.classifiers.knn;

import ets.gti770.spam.classifiers.ISpamClassifierStrategy;
import weka.core.Instances;

/**
 * This class trains and classifies data following the KNN
 *  algorithm.
 *  
 * @author Jean-Philippe Leclerc
 * @author Jonathan Saindon
 * @author Simon Robert
 * @version 2017-10-25
 */
public class KNNSpamClassifier implements ISpamClassifierStrategy 
{
	@Override
	public int[] classify(Instances trainData, Instances inputData)
	{
		throw new java.lang.UnsupportedOperationException(
				"The KNN classifier is not supported yet.");
	}
}
