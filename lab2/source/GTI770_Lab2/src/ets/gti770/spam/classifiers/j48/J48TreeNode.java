package ets.gti770.spam.classifiers.j48;

import java.util.LinkedList;

import ets.gti770.spam.utils.DataSet;

public class J48TreeNode 
{
	private int level; // How deep in the tree
	private LinkedList<J48TreeNode> children;
	private DataSet dataSet;

	public J48TreeNode(int level, DataSet dataSet)
	{
		this.level = level;
		this.dataSet = dataSet;
		this.children = new LinkedList<J48TreeNode>();
	}
}
