package ets.gti770.spam.utils;

import weka.core.Instances;

public class DataSet {

	public Instances instances;
	public int numSpam;
	public int numNonSpam;
	public int totalValues;
	
	public DataSet(Instances instances, int numSpam, int numNonSpam)
	{
		this.instances = instances;
		this.numSpam = numSpam;
		this.numNonSpam = numNonSpam;
		this.totalValues = numSpam + numNonSpam;
	}
	
	public DataSet(Instances instances)
	{
		int[] counts = instances.attributeStats(instances.classIndex()).nominalCounts;
		
		this.numNonSpam = counts[0];
		this.numSpam = counts[1];
		
		this.totalValues = numSpam + numNonSpam;
	}
}
