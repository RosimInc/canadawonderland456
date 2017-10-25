package ets.gti770.spam.app;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import ets.gti770.spam.classifiers.ISpamClassifierStrategy;
import ets.gti770.spam.classifiers.bayes.BayesSpamClassifier;
import ets.gti770.spam.classifiers.j48.J48SpamClassifier;
import ets.gti770.spam.classifiers.knn.KNNSpamClassifier;

import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

/**
 * This application uses various machine learning algorithms in order
 *  to classify input data about email, to determine if they contain
 *  spam or not.
 * @author Jean-Philippe Leclerc
 * @author Jonathan Saindon
 * @author Simon Robert
 * @version 2017-10-25
 */
public class SpamAnalysisApp
{
	// Available classifiers
	private enum ClassifierID {Bayes, KNN, J48, NONE}
	private static ISpamClassifierStrategy[] classifiers = {
			new BayesSpamClassifier(),
			new KNNSpamClassifier(),
			new J48SpamClassifier()
	};
	
	// Run options
	private static final ClassifierID BestClassifier = ClassifierID.J48;
	private static final ClassifierID WorstClassifier = ClassifierID.Bayes;
	private static final String trainFileName = "train-data.arff";
	
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
		
		DataSource sourceTrain = null;
		DataSource sourceInput = null;
		
		// Load the local resource
		// This allows the file to be packaged inside the Jar
		String localTrainFileName = "";
		try {
			localTrainFileName = URLDecoder.decode(
					SpamAnalysisApp.class.getResource(trainFileName).getPath(), "UTF-8");
			System.out.println(localTrainFileName);
		
			// Load the training data
			sourceTrain = new DataSource(localTrainFileName);
			Instances trainData = sourceTrain.getDataSet();
			if (trainData.classIndex() == -1)
				trainData.setClassIndex(trainData.numAttributes() - 1);
			
			// Load the analysis data
			sourceInput = new DataSource(inputFile);
			Instances inputData = sourceInput.getDataSet();
			if (inputData.classIndex() == -1)
				inputData.setClassIndex(inputData.numAttributes() - 1);
			
			// Use the best method
			analyzeData(trainData, inputData, BestClassifier, outputFileBest);
			
			// Use the worst method
			analyzeData(trainData, inputData, WorstClassifier, outputFileWorst);
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
	private static void analyzeData(Instances trainData, Instances inputData, 
			ClassifierID method, String outputFileName) throws FileNotFoundException
	{
		PrintWriter pw = null;
		
		try
		{
			System.out.println("Method: " + method.name());
			
			// Use the specified method to classify the input data
			int[] results = classifiers[method.ordinal()].classify(trainData, inputData);
			
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
