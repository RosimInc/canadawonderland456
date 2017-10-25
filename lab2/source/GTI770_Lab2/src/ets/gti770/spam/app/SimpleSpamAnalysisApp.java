package ets.gti770.spam.app;

import java.io.InputStream;
import java.io.PrintWriter;

import weka.classifiers.Classifier;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.ConverterUtils.DataSource;

/**
 * This application uses Weka models in order to classify
 *  some unlabeled information.
 *  
 * @author Jean-Philippe Leclerc
 * @author Jonathan Saindon
 * @author Simon Robert
 * @version 2017-10-25
 */
public class SimpleSpamAnalysisApp
{
	// Available classifiers
	private enum ClassifierID { Bayes, KNN, J48, NONE }
	private static String[] classifiers = {
			"bayes.model",
			"knn.model",
			"j48.model"
	};
	
	// Run options
	private static final ClassifierID BestClassifier = ClassifierID.J48;
	private static final ClassifierID WorstClassifier = ClassifierID.Bayes;
	
	/*
	 * Launches the application
	 */
	public static void main(String[] args)
	{
		// Input validation
		if(args.length < 3)
		{
			System.out.println("Error - Wrong input parameters");
			System.out.println("Usage:");
			System.out.println(" in - Input file name");
			System.out.println(" ob - Output file name for best solution");
			System.out.println(" ow - Output file name for worst solution");
			System.exit(1);
		}
		
		// Argument extraction
		String inputFile = args[0];
		String outputFileBest = args[1];
		String outputFileWorst = args[2];
		
		DataSource sourceInput = null;
		
		try 
		{
			// Load the analysis data
			sourceInput = new DataSource(inputFile);
			Instances inputData = sourceInput.getDataSet();
			if (inputData.classIndex() == -1)
				inputData.setClassIndex(inputData.numAttributes() - 1);
			
			// Use the best method
			analyzeData(inputData, BestClassifier, outputFileBest);
			
			// Use the worst method
			analyzeData(inputData, WorstClassifier, outputFileWorst);
		} 
		catch (Exception e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	/*
	 * Trains and analyzes data using a specific classifier method
	 */
	private static void analyzeData(Instances inputData, 
			ClassifierID method, String outputFileName) throws Exception
	{
		PrintWriter pw = null;
		
		try
		{
			System.out.println("Method: " + method.name());
			
			int[] results = new int[inputData.size()];

			// Load the local resource
			// This allows the file to be packaged inside the Jar	
			InputStream is = SimpleSpamAnalysisApp.class.
					getResourceAsStream(classifiers[method.ordinal()]);
		
			// Generate the classifier from model
			Classifier classifier = (Classifier) SerializationHelper.read(is);
			
			int numInstances = inputData.size();
			
			// Compute the expected value of each instance
			for(int i=0; i<numInstances; i++)
			{
				results[i] = (int)classifier.classifyInstance(inputData.get(i));
			}

			// Output the results to the file
			pw = new PrintWriter(outputFileName);
			for(int r : results)
				pw.println(r);
		}
		catch(Exception e)
		{
			throw e;
		}
		finally
		{
			// Make sure the file stream is closed
			if(pw != null)
				pw.close();
		}
	}
}
