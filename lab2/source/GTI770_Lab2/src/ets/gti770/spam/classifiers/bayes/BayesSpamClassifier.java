package ets.gti770.spam.classifiers.bayes;

import ets.gti770.spam.classifiers.ISpamClassifierStrategy;
import weka.core.Instances;

/**
 * This class trains and classifies data following the Bayes
 *  algorithm.
 * @author Jean-Philippe Leclerc
 * @author Jonathan Saindon
 * @author Simon Robert
 * @version 2017-10-25
 */
public class BayesSpamClassifier implements ISpamClassifierStrategy 
{
	@Override
	public int[] classify(Instances trainData, Instances inputData) 
	{
		int[] results = new int[inputData.size()];
		
		// JS Bayes - Train data
		
		// JS Bayes - Classify input data

		// JS Bayes - Make sure all new classes are package
		
		return results;
	}
}
