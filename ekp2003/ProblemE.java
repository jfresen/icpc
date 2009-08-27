package ekp2003;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class ProblemE
{
	
	public static void main(String[] args) throws Throwable
	{
		new ProblemE();
	}
	
	public ProblemE() throws Throwable
	{
		Scanner in = new Scanner(new File("ekp2003\\sampledata\\e.in"));
		int cases = in.nextInt();
		while (cases-- > 0)
		{
			char[] s = in.next().toCharArray();
			char[] t = in.next().toCharArray();
			Node sroot = new Node(null);
			Node troot = new Node(null);
			makeTree(sroot, s);
			makeTree(troot, t);
			sroot.makeChildren();
			troot.makeChildren();
			if (isomorph(sroot, troot))
				System.out.println("same");
			else
				System.out.println("different");
		}
	}
	
	private boolean isomorph(Node n, Node m)
	{
		if (n.successors != m.successors || n.children.length != m.children.length)
			return false;
		if (n.successors == 0)
			return true;
		boolean found = true;
		boolean[] used = new boolean[m.children.length];
		for (int i = 0; i < n.children.length && found; i++)
		{
			found = false;
			for (int j = 0; j < m.children.length && !found; j++)
			{
				if (used[j]) continue;
				used[j] = found = isomorph(n.children[i], m.children[j]);
			}
		}
		return found;
	}
	
	private void makeTree(Node root, char[] s)
	{
		for (int i = 0; i < s.length; i++)
		{
			if (s[i] == '0')
			{
				Node n = new Node(root);
				root.childrenArray.add(n);
				root = n;
			}
			else
			{
				int successors = root.successors + 1;
				root.makeChildren();
				root = root.parent;
				root.successors += successors;
			}
		}
	}
	
	private class Node
	{
		public Node parent = null;
		public ArrayList<Node> childrenArray = new ArrayList<Node>();
		public Node[] children;
		public int successors = 0;
		public Node(Node parent)
		{this.parent = parent;}
		public void makeChildren()
		{
			children = new Node[childrenArray.size()];
			childrenArray.toArray(children);
			childrenArray = null;
		}
	}
}

