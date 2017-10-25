package ets.gti770.spam.classifiers.j48;

import java.util.LinkedList;

import weka.core.Instances;

public class J48TreeNode {
	private int level; // How deep in the tree
	private LinkedList<J48TreeNode> children = new LinkedList<J48TreeNode>();
	private Instances instances;
	
	private int numSpam = 0;
	private int numNotSpam = 0;
	
	public J48TreeNode(int level, Instances instances)
	{
		this.level = level;
		this.instances = instances;
		
	}
}
