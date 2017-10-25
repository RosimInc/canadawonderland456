package ets.gti770.spam.classifiers.j48;

import weka.core.Instance;

/**
 * This class represents a tree node in the J48
 *  classification algorithm.
 *  
 * @author Jean-Philippe Leclerc
 * @author Jonathan Saindon
 * @author Simon Robert
 * @version 2017-10-25
 */
public abstract class J48TreeNode 
{
	/**
	 * This method returns the value determined by the tree node
	 *  or its potential children, given a specific instance.
	 * @param instance The instance to classify
	 * @return The classification
	 */
	public abstract int getSpamValue(Instance instance);
}
