package ets.gti770.spam.classifiers.j48;

import java.util.Random;

import ets.gti770.spam.classifiers.ISpamClassifierStrategy;
import ets.gti770.spam.utils.DataSet;
import weka.core.Instance;
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
	private static final double trainingProportion = 0.95;
	
	@Override
	public int[] classify(Instances trainData, Instances inputData) 
	{
		int[] results = new int[inputData.size()];
		
		J48TreeNode treeRoot = trainClassifier(trainData);
		
		int numInput = inputData.size();
		
		for(int i=0; i<numInput; i++)
		{
			results[i] = treeRoot.getSpamValue(inputData.get(i));
		}
		
		return results;
	}
	
	public J48TreeNode trainClassifier(Instances trainData)
	{
		DataSet dataSet = new DataSet(trainData);
		return J48Utils.createNode(dataSet);
	}
	
	/**
	 * This test method computes 
	 * @param trainData
	 */
	public void testClassify(Instances trainData) 
	{
		Instances subset = new Instances(trainData);
		
		int numInstances = subset.size();
		int numTraining = (int)(numInstances * trainingProportion);
		
		subset.randomize(new Random(System.currentTimeMillis()));
		Instances train = new Instances(
				subset, 0, numTraining);
		Instances valid = new Instances(
				subset,numTraining, numInstances-numTraining);
		
		J48TreeNode treeRoot = trainClassifier(train);
		
		validate(treeRoot, valid);
	}

	/*
	 * Tests values for a specific subset of the training data.
	 */
	private void validate(J48TreeNode treeRoot, Instances instances)
	{
		int numCorrect = 0;
		int numIncorrect = 0;
		
		// For each instance, determine if the prediction is true
		int expected, actual;
		for(Instance i : instances)
		{
			expected = (int)i.classValue();
			actual = treeRoot.getSpamValue(i);
			
			if(expected == actual)
				numCorrect++;
			else
				numIncorrect++;
		}
		
		System.out.println(numCorrect * 1.0 / (numCorrect+numIncorrect));
	}
}
