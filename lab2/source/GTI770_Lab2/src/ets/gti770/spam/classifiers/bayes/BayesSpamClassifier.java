package ets.gti770.spam.classifiers.bayes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

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
		
		// Init arraylist of attributes data
		Enumeration<Attribute> enumAttr = trainData.enumerateAttributes();
		while (enumAttr.hasMoreElements()) {
			attributes.add(new BayesAttribute(enumAttr.nextElement()));
		}
		
		// Parse training data into attributes data
		Attribute classAttr = trainData.classAttribute();
		Enumeration<Instance> enumInstances = trainData.enumerateInstances();
		while (enumInstances.hasMoreElements()) {
			Instance data = enumInstances.nextElement();
			double spamValue = data.value(classAttr);
			boolean isSpam = spamValue == 1.0;
			
			for (BayesAttribute attr : attributes) {
				double value = data.value(attr.getWekaAttribute());
				attr.parse(value, isSpam);
			}
		}

		ArrayList<BayesAttribute> goodAttributes = new ArrayList<>();
		for (BayesAttribute attr : attributes) {
			double spamRatio = attr.getSpamProbability();
			if (spamRatio > 0.4) {
				goodAttributes.add(attr);
			}
		}
		
		for (BayesAttribute attr : goodAttributes) {
			System.out.println(attr.getWekaAttribute().name() + ", ratio: " + attr.getSpamProbability());
		}
		
		// JS Bayes - Classify input data

		// JS Bayes - Make sure all new classes are package
		
		return results;
	}
}
