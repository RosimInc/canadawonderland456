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
	/**
	 * This utility method dynamically creates a tree node according
	 *  to specific criteria.
	 * @param dataSet The data set
	 * @param level The depth in the tree
	 * @return The created node
	 */
	public static J48TreeNode createNode(DataSet dataSet, int level)
	{
		// Value node if there is only one type of value
		if(dataSet.numNonSpam * dataSet.numSpam == 0)
			return new J48ValueNode(dataSet);
		
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
		
		// SR Add decision to stop going further, according to level
		
		// If nothing is better, make a value node with the most found value
		if(bestSplit == null)
			return new J48ValueNode(dataSet);
		
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
