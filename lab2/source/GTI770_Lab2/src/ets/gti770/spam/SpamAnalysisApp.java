package ets.gti770.spam;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import ets.gti770.spam.classifiers.ISpamClassifierStrategy;
import ets.gti770.spam.classifiers.bayes.BayesSpamClassifier;
import ets.gti770.spam.classifiers.j48.J48SpamClassifier;
import ets.gti770.spam.classifiers.knn.KNNSpamClassifier;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;


public class SpamAnalysisApp
{
	private enum Classifier {Bayes, KNN, J48, NONE}
	private static ISpamClassifierStrategy[] classifiers = {
			new BayesSpamClassifier(),
			new KNNSpamClassifier(),
			new J48SpamClassifier()
	};
	
	// Run options
	private static final boolean isUsingFolders = true;
	private static final Classifier BestClassifier = Classifier.J48;
	private static final Classifier WorstClassifier = Classifier.Bayes;
	private static final String trainFileName = "train/spamdata-dev-E16.arff";
	
	
	public static void main(String[] args)
	{
		// Input validation
		if(args.length < 3)
		{
			System.out.println("Error - Wrong input parameters");
			System.out.println("Usage:");
			System.out.println(" i  - Input file name");
			System.out.println(" ob - Output file name for best solution");
			System.out.println(" ow - Output file name for worst solution");
			System.exit(1);
		}
				
		String i = args[0];
		String ob = args[1];
		String ow = args[2];
		
		DataSource sourceTrain = null;
		DataSource sourceInput = null;
		
		try 
		{
			sourceTrain = new DataSource(trainFileName);
			Instances trainData = sourceTrain.getDataSet();
			if (trainData.classIndex() == -1)
				trainData.setClassIndex(trainData.numAttributes() - 1);
			
			sourceInput = new DataSource((isUsingFolders ? "input/" : "") + i);
			Instances inputData = sourceInput.getDataSet();
			if (inputData.classIndex() == -1)
				inputData.setClassIndex(inputData.numAttributes() - 1);
			
			// Use the best method
			analyzeData(trainData, inputData, BestClassifier, ob);
			
			// Use the worst method
			analyzeData(trainData, inputData, WorstClassifier, ow);
		} 
		catch (Exception e)
		{
			e.printStackTrace();
			System.exit(1);
			
		}
	}
	
	private static void analyzeData(Instances trainData, Instances inputData, Classifier method, String outputFileName) throws FileNotFoundException
	{
		PrintWriter pw = null;
		
		try
		{
			System.out.println("Method: " + method.name());
			
			int[] results = classifiers[method.ordinal()].classify(trainData, inputData);
			
			pw = new PrintWriter((isUsingFolders ? "output/" : "") + outputFileName);
			
			for(int r : results)
				pw.println(r);
		}
		catch(Exception e)
		{
			throw e;
		}
		finally
		{
			if(pw != null)
				pw.close();
		}
	}
}
