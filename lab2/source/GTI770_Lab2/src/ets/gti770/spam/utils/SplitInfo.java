package ets.gti770.spam.utils;

/**
 * This read-only class gives information on a dataset split.
 *  
 * @author Jean-Philippe Leclerc
 * @author Jonathan Saindon
 * @author Simon Robert
 * @version 2017-10-25
 */
public class SplitInfo 
{
	public final int attributeIndex;
	public final int splitIndex;
	public final double splitValue;
	public final double splitGain;
	
	/**
	 * Constructor
	 * @param attributeIndex
	 * @param splitIndex
	 * @param splitValue
	 * @param splitGain
	 */
	public SplitInfo(int attributeIndex, int splitIndex, double splitValue, double splitGain)
	{
		this.attributeIndex = attributeIndex;
		this.splitIndex = splitIndex;
		this.splitValue = splitValue;
		this.splitGain = splitGain;
	}
}
