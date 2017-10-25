package ets.gti770.spam.classifiers.j48;

import ets.gti770.spam.utils.DataSet;
import weka.core.Instance;

/**
 * This class represents a tree node handling a single
 *  value.
 *  
 * @author Jean-Philippe Leclerc
 * @author Jonathan Saindon
 * @author Simon Robert
 * @version 2017-10-25
 */
class J48ValueNode extends J48TreeNode
{
	private int value;
	
	/**
	 * This constructor takes the most present value
	 *  in the data set as the node value.
	 * @param dataSet The data set
	 */
	public J48ValueNode(DataSet dataSet)
	{
		this.value = dataSet.valueMostOften;
	}
	
	@Override
	public int getSpamValue(Instance instance) 
	{
		return value;
	}
}
