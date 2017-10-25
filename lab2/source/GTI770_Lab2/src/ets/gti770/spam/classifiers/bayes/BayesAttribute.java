package ets.gti770.spam.classifiers.bayes;

import weka.core.Attribute;

/**
 * This class trains and classifies data following the Bayes
 *  algorithm.
 * @author Jean-Philippe Leclerc
 * @author Jonathan Saindon
 * @author Simon Robert
 * @version 2017-10-24
 */
class BayesAttribute {
	private int spam;
	private int notSpam;
	private int hasValueWhenSpam;
	private int hasValueWhenNotSpam;
	private Attribute wekaAttribute;
	
	public BayesAttribute(Attribute wekaAttribute) {
		this.wekaAttribute = wekaAttribute;
		this.spam = 0;
		this.notSpam = 0;
		this.hasValueWhenSpam = 0;
		this.hasValueWhenNotSpam = 0;
	}
	
	public void parse(double value, boolean isSpam) {
		if (isSpam) {
			this.spam++;
			
			if (value > 0.0)
				hasValueWhenSpam++;
		}
		else {
			this.notSpam++;

			if (value > 0.0)
				hasValueWhenNotSpam++;
		}
	}
	
	public double getSpamProbability() {
		return (double)this.spam / (double)(this.spam + this.notSpam);
	}
	
	public double getValuePresenceRatio() {
		return (double)this.hasValueWhenSpam / (double)(this.hasValueWhenSpam + this.hasValueWhenNotSpam);
	}
	
	public Attribute getWekaAttribute() {
		return this.wekaAttribute;
	}
}
