package ets.gti770.spam.classifiers.j48;

// SR Remove code
//import ets.gti770.spam.utils.DataSet;
import weka.core.Instance;

public abstract class J48TreeNode 
{
	// SR Remove code
	/*private DataSet dataSet;

	public J48TreeNode(DataSet dataSet)
	{
		this.dataSet = dataSet;
	}*/
	
	public abstract int getSpamValue(Instance instance);
}
