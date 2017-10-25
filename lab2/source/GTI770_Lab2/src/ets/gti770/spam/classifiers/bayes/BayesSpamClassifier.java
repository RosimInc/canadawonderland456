package ets.gti770.spam.classifiers.bayes;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Random;

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
	public int[] classify(Instances data, Instances inputData) 
	{
		int[] results = new int[inputData.size()];
		
		// JS Bayes - Train data
		
		int nInstances = data.size();
		int nTraining = (int)(nInstances * 0.9);
		
		// Shuffle the values
		data.randomize(new Random(System.currentTimeMillis()));
		
		// Split between training and validation data
		Instances trainData = new Instances(data, 0, nTraining);
		Instances validData = new Instances(data, nTraining, nInstances - nTraining);
		
		// Do the actual training
		ArrayList<BayesAttribute> significativeAttributes = train(trainData);
		
		
		// JS Bayes - Validate data
		testClassify(validData, significativeAttributes);

		
		// JS Bayes - Classify input data
		int i = 0;
		int nPossibleFlags = significativeAttributes.size();
		Enumeration<Instance> enumData = inputData.enumerateInstances();
		while (enumData.hasMoreElements()) {
			int flagsCount = 0;
			
			Instance element = enumData.nextElement();
			// Check presence of value (rise flag) for every significative attribute
			for (BayesAttribute attr : significativeAttributes) {
				double value = element.value(attr.getWekaAttribute());
				
				if (value > 0.0) {
					flagsCount++;
				}
			}
			
			// Flag as spam if at least half the flags were risen
			if (flagsCount > (nPossibleFlags / 2)) {
				results[i] = 1;
			}
			i++;
		}
		
		return results;
	}

	/**
	 * Train with data to determine the best attributes to use for data validation
	 * according to Bayes probability analysis
	 * 
	 * @param trainData
	 * @return goodAttributes
	 */
	private ArrayList<BayesAttribute> train(Instances trainData) {
		ArrayList<BayesAttribute> attributes = new ArrayList<>();
		
		// Init arraylist of attributes data
		Enumeration<Attribute> enumAttr = trainData.enumerateAttributes();
		while (enumAttr.hasMoreElements()) {
			attributes.add(new BayesAttribute(enumAttr.nextElement()));
		}
		
		// Parse training data into attributes data
		Attribute classAttr = trainData.classAttribute();
		Enumeration<Instance> enumTrain = trainData.enumerateInstances();
		while (enumTrain.hasMoreElements()) {
			Instance data = enumTrain.nextElement();
			double spamValue = data.value(classAttr);
			boolean isSpam = spamValue == 1.0;
			
			for (BayesAttribute attr : attributes) {
				double value = data.value(attr.getWekaAttribute());
				attr.parse(value, isSpam);
			}
		}
	
		// Simplify attributes to keep only the most significative ones
		ArrayList<BayesAttribute> goodAttributes = new ArrayList<>();
		for (BayesAttribute attr : attributes) {
			double spamRatio = attr.getSpamProbability();
			if (spamRatio > 0.4) {
				goodAttributes.add(attr);
			}
		}
		
		return goodAttributes;
	}
	
	/**
	 * Use the previously established attributes to validate predictions on validation data
	 * 
	 * @param data
	 * @param attributes
	 */
	private void testClassify(Instances validData, ArrayList<BayesAttribute> attributes) 
	{
		Attribute classAttr = validData.classAttribute();
		int nPossibleFlags = attributes.size();

		int success = 0, error = 0;
		Enumeration<Instance> enumData = validData.enumerateInstances();
		while (enumData.hasMoreElements()) {
			int flagsCount = 0;
			
			Instance element = enumData.nextElement();
			// Check presence of value (rise flag) for every attribute
			for (BayesAttribute attr : attributes) {
				double value = element.value(attr.getWekaAttribute());
				
				if (value > 0.0) {
					flagsCount++;
				}
			}
			
			// Flag as spam if at least half the flags were risen
			int result = 0;
			if (flagsCount > (nPossibleFlags / 2)) {
				result = 1;
			}
			
			double expected = element.value(classAttr);
			if (expected == result) {
				success++;
			} else {
				error++;
			}
		}
		
		System.out.println("Accuracy: " + (double)success / (double)(success + error));
	}
}