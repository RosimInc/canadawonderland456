package ets.gti770.spam.classifiers.bayes;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;

import ets.gti770.spam.classifiers.ISpamClassifierStrategy;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

/**
 * This class trains and classifies data following the Bayes
 *  algorithm.
 * @author Jean-Philippe Leclerc
 * @author Jonathan Saindon
 * @author Simon Robert
 * @version 2017-10-25
 */
public class BayesSpamClassifier implements ISpamClassifierStrategy 
{
	@Override
	public int[] classify(Instances trainData, Instances inputData) 
	{
		int[] results = new int[inputData.size()];
		ArrayList<BayesAttribute> attributes = new ArrayList<>();
		
		// JS Bayes - Train data
		
		// Init hashmap and attributes data
		Enumeration<Attribute> enumAttr = trainData.enumerateAttributes();
		while (enumAttr.hasMoreElements()) {
			attributes.add(new BayesAttribute(enumAttr.nextElement()));
		}
		Attribute classAttr = attributes.get(attributes.size() - 1).getWekaAttribute();
		
		// Parse train data into attributes data
		Enumeration<Instance> enumInstances = trainData.enumerateInstances();
		while (enumInstances.hasMoreElements()) {
			Instance data = enumInstances.nextElement();
			boolean isSpam = data.value(classAttr) == 1;
			for (BayesAttribute attr : attributes) {
				attr.parse(data.value(attr.getWekaAttribute()), isSpam);
			}
		}
		
		// JS Bayes - Classify input data

		// JS Bayes - Make sure all new classes are package
		
		return results;
	}
}
