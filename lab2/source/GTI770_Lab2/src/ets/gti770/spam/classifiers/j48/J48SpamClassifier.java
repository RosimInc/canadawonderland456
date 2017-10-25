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
		
		// Generate the tree
		J48TreeNode treeRoot = trainClassifier(trainData);
		
		// Predict all values
		int numInput = inputData.size();
		for(int i=0; i<numInput; i++)
		{
			results[i] = treeRoot.getSpamValue(inputData.get(i));
		}
		
		return results;
	}
	
	/**
	 * This method creates a tree out of training data.
	 * @param trainData The training data
	 * @return The generated decision tree
	 */
	public J48TreeNode trainClassifier(Instances trainData)
	{
		DataSet dataSet = new DataSet(trainData);
		return J48Utils.createNode(dataSet);
	}
	
	/**
	 * This test method computes the accuracy of the algorithm
	 *  by separating the data for training and validation.
	 * @param data The data used for supervised learning
	 */
	public void testClassify(Instances data) 
	{
		int numInstances = data.size();
		int numTraining = (int)(numInstances * trainingProportion);
		
		// Shuffle the values
		data.randomize(new Random(System.currentTimeMillis()));
		
		// Split between training and validation data
		Instances trainData = new Instances(
				data, 0, numTraining);
		Instances validData = new Instances(
				data,numTraining, numInstances-numTraining);
		
		// Build the tree
		J48TreeNode treeRoot = trainClassifier(trainData);
		
		// Validation
		
		int numCorrect = 0;
		int numIncorrect = 0;
		
		// For each instance, determine if the prediction is true
		int expected, actual;
		for(Instance i : validData)
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
