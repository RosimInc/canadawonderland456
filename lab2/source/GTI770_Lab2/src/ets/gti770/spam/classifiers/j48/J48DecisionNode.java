package ets.gti770.spam.classifiers.j48;

import ets.gti770.spam.utils.DataSet;
import ets.gti770.spam.utils.SplitInfo;
import weka.core.Instance;
import weka.core.Instances;

/**
 * This class represents a tree node handling a continuous
 *  variable decision to perform a binary split.
 *  
 * @author Jean-Philippe Leclerc
 * @author Jonathan Saindon
 * @author Simon Robert
 * @version 2017-10-25
 */
class J48DecisionNode extends J48TreeNode 
{
	private J48TreeNode lNode;
	private J48TreeNode rNode;
	private SplitInfo splitInfo;
	
	/**
	 * This constructor handles the recursive creation of children
	 *  nodes.
	 * @param dataSet The data set
	 * @param splitInfo The splitting information
	 * @param level The depth in the tree
	 */
	public J48DecisionNode(DataSet dataSet, SplitInfo splitInfo, int level)
	{
		this.splitInfo = splitInfo;
		
		Instances instances = dataSet.instances;
		int splitIndex = splitInfo.splitIndex;
		
		// Split the data in two
		DataSet lDataSet = new DataSet(new Instances(instances, 
				0, splitIndex));
		DataSet rDataSet = new DataSet(new Instances(instances, 
				splitIndex, instances.numAttributes() - splitIndex));
		
		// Create the children nodes
		this.lNode = J48Utils.createNode(lDataSet, level + 1);
		this.rNode = J48Utils.createNode(rDataSet, level + 1);
		
		// SR Test the recursive creation
	}
	
	@Override
	public int getSpamValue(Instance instance)
	{
		// SR Test get spam value
		
		// Checks the split decision to determine the child to call
		if(instance.value(splitInfo.attributeIndex) < splitInfo.splitValue)
			return lNode.getSpamValue(instance);
		else		
			return rNode.getSpamValue(instance);
	}

}
