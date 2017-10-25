package ets.gti770.spam.classifiers.j48;

import ets.gti770.spam.utils.DataSet;
import ets.gti770.spam.utils.LearningUtils;
import ets.gti770.spam.utils.SplitInfo;

/**
 * This utility class allows the dynamic creation of tree nodes.
 *  
 * @author Jean-Philippe Leclerc
 * @author Jonathan Saindon
 * @author Simon Robert
 * @version 2017-10-25
 */
class J48Utils 
{
	private static final int maxTreeDepth = 10;
	
	/**
	 * This utility method dynamically creates a tree node according
	 *  to specific criteria.
	 * @param dataSet The data set
	 * @param level The depth in the tree
	 * @return The created node
	 */
	public static J48TreeNode createNode(DataSet dataSet, int level)
	{
		// SR Remove
		/*System.out.println(dataSet.numNonSpam + " " + dataSet.numSpam);*/
		
		// Value node if there is only one type of value
		if(dataSet.numNonSpam * dataSet.numSpam == 0)
		{
			// SR Remove
			/*System.out.println("   Zero");*/
			return new J48ValueNode(dataSet);
		}
		
		// Value node if depth is greater than a threshold
		if(level > maxTreeDepth)
		{
			// SR Remove
			/*System.out.println("   Max depth");*/
			return new J48ValueNode(dataSet);
		}
		
		int numAttributes = dataSet.instances.numAttributes();
		double bestGain = 0;
		SplitInfo cSplit;
		SplitInfo bestSplit = null;
		
		// Determine the best attribute to split
		for(int i=0; (i+1) < numAttributes; i++)
		{
			cSplit = LearningUtils.getBestSplit(dataSet, i);
			
			// Only consider the attribute if gain is better
			if(cSplit.splitGain > bestGain)
			{
				bestGain = cSplit.splitGain;
				bestSplit = cSplit;
			}
		}
		
		// SR Remove
		/*System.out.printf("  Best gain + %4.3f\n", bestGain);*/
		
		// If nothing is better, make a value node with the most found value
		if(bestSplit == null)
		{
			// SR Remove
			/*System.out.println("   Oops");*/
			return new J48ValueNode(dataSet);
		}

		// SR Remove
		/*System.out.println("   Alright");*/
		
		// Create a decision node with the computed split
		return new J48DecisionNode(dataSet, bestSplit, level);
	}
	
	/**
	 * This utility method dynamically creates a tree node according
	 *  to specific criteria. It assumes the tree depth is 1.
	 * @param dataSet The data set
	 * @return The created node
	 */
	public static J48TreeNode createNode(DataSet dataSet)
	{
		return createNode(dataSet, 1);
	}
}
