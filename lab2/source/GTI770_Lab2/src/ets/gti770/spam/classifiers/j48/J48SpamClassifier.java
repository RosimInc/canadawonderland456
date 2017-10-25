package ets.gti770.spam.classifiers.j48;

import ets.gti770.spam.classifiers.ISpamClassifierStrategy;
import ets.gti770.spam.utils.DataSet;
import weka.core.Instances;

/**
 * This class trains and classifies data following the J48/C4.5
 *  algorithm.
 *  
 * @author Jean-Philippe Leclerc
 * @author Jonathan Saindon
 * @author Simon Robert
 * @version 2017-10-25
 */
public class J48SpamClassifier implements ISpamClassifierStrategy
{
	private static final double trainingProportion = 0.9;
	
	@Override
	public int[] classify(Instances trainData, Instances inputData) 
	{
		int[] results = new int[inputData.size()];
		
		// SR J48 - Train data
		
		/*Instances subset = new Instances(trainData);
		
		int numInstances = subset.size();
		int numTraining = (int)(numInstances * trainingProportion);
		
		subset.randomize(new Random(System.currentTimeMillis()));
		Instances train = new Instances(
				subset, 0, numTraining);
		Instances valid = new Instances(
				subset,numTraining, numInstances-numTraining);*/
		
		DataSet dataSet = new DataSet(trainData);
		/*System.out.println(set.entropy);
		System.out.println(set.numNonSpam);
		System.out.println(set.numSpam);
		System.out.println(set.totalValues);*/
		
		/*SplitInfo info = LearningUtils.getBestSplit(set, 2);
		System.out.println(info.splitValue);*/
		
		J48TreeNode treeRoot = J48Utils.createNode(dataSet);
		
		
		// SR J48 - Classify input data
		
		return results;
	}
}
