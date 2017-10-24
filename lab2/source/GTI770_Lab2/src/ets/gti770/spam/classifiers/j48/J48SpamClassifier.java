package ets.gti770.spam.classifiers.j48;

import ets.gti770.spam.classifiers.ISpamClassifierStrategy;
import weka.core.Instances;

/**
 * This class trains and classifies data following the J48/C4.5
 *  algorithm.
 * @author Jean-Philippe Leclerc
 * @author Jonathan Saindon
 * @author Simon Robert
 * @version 2017-10-25
 */
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
