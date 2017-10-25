package ets.gti770.spam.utils;

import weka.core.Instances;

/**
 * This read-only structure gives info on a dataset of instances.
 * 
 * @author Jean-Philippe Leclerc
 * @author Jonathan Saindon
 * @author Simon Robert
 * @version 2017-10-25
 */
public class DataSet 
{
	public final Instances instances;
	public final int numSpam;
	public final int numNonSpam;
	public final int totalValues;
	public final int valueMostOften;
	public final double entropy;
	
	public DataSet(Instances instances)
	{
		this.instances = instances;
		
		int[] counts = instances.attributeStats(instances.classIndex()).nominalCounts;
		
		this.numNonSpam = counts[0];
		this.numSpam = counts[1];
		
		valueMostOften = (numNonSpam > numSpam ? 0 : 1);
		
		this.totalValues = numSpam + numNonSpam;
		
		this.entropy = LearningUtils.getEntropy(numSpam, numNonSpam);
	}
}
