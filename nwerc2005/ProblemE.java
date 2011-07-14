package nwerc2005;

import java.io.File;
import java.util.Scanner;

public class ProblemE
{
	
	static int n, m;
	static int[] p, l, r; // parent, leftChild and rightChild arrays
	static boolean[] t;   // trapped or not
	static char[] s;      // state: [c]lear, [b]locked or [p]artially blocked
	
	public static void main(String[] args) throws Throwable
	{
		Scanner in = new Scanner(new File("nwerc2005/testdata/e.in"));
		int casus = 0;
		while ((n=in.nextInt()) != 0)
		{
			casus++;
			m = in.nextInt();
			p = new int[n+1];
			l = new int[n+1];
			r = new int[n+1];
			t = new boolean[n+1];
			s = new char[n+1];
			for (int i = 1; i <= n; i++)
			{
				int x = in.nextInt();
				if (x > 0) l[i] = in.nextInt();
				if (x > 1) r[i] = in.nextInt();
				if (x > 2) p[i] = in.nextInt();
			}
			setLinks(l[1], 1);
			for (int i = 0; i < m; i++)
				t[in.nextInt()] = true;
			System.out.println(nrOfGateways(1) + (s[1]!='b'?1:0));
		}
	}
	
	private static void setLinks(int node, int parent)
	{
		while (p[node] != parent)
		{
			int x = l[node];
			l[node] = r[node];
			r[node] = p[node];
			p[node] = x;
		}
		if (l[node] == 0 && r[node] != 0)
		{
			l[node] = r[node];
			r[node] = 0;
		}
		if (l[node] != 0)
			setLinks(l[node], node);
		if (r[node] != 0)
			setLinks(r[node], node);
	}
	
	private static int nrOfGateways(int n)
	{
		if (t[n])
		{
			s[n] = 'b';
			return 0;
		}
		// First, process the children:
		int g = 0;
		if (l[n] != 0) g += nrOfGateways(l[n]);
		if (r[n] != 0) g += nrOfGateways(r[n]);
			
		// Second, set the state of this node:
		// no children
		if (l[n] == 0)
			s[n] = t[n] ? 'b' : 'c';
		// one child
		else if (r[n] == 0)
			s[n] = t[n] ? 'b' : s[l[n]];
		// two children
		else if (t[n] || (s[l[n]] != 'c' && s[r[n]] != 'c'))
			s[n] = 'b';
		else if (s[l[n]] == 'c')
			s[n] = s[r[n]] == 'c' ? 'c' : 'p';
		else if (s[r[n]] == 'c')
			s[n] = s[l[n]] == 'c' ? 'c' : 'p';
		
		// Third, count extra gateways:
		// no children
		if (l[n] == 0)
			return g+0;
		// one child
		else if (r[n] == 0)
			return g+(t[n] && s[l[n]]!='b' ? 1 : 0);
		// two children
		else if (s[n] != 'b' || (s[l[n]] == 'b' && s[r[n]] == 'b'))
			return g+0;
		else if (s[l[n]] != 'b' && s[r[n]] != 'b')
			return g+2;
		else
			return g+1;
	}
	
//	Non-recursive solution, since the recursive solution will run into problems
//	on deep trees, such as one with 50k chained nodes.
//	
//	private static void setLinks()
//	{
//		int parent = 1, node = l[1];
//		Stack<Integer> stack = new Stack<Integer>();
//		stack.push(node);
//		stack.push(parent);
//		while (!stack.isEmpty())
//		{
//			parent = stack.pop();
//			node = stack.pop();
//			while (p[node] != parent)
//			{
//				int x = l[node];
//				l[node] = r[node];
//				r[node] = p[node];
//				p[node] = x;
//			}
//			if (l[node] == 0 && r[node] != 0)
//			{
//				l[node] = r[node];
//				r[node] = 0;
//			}
//			if (l[node] != 0)
//			{
//				stack.push(l[node]);
//				stack.push(node);
//			}
//			if (r[node] != 0)
//			{
//				stack.push(r[node]);
//				stack.push(node);
//			}
//		}
//	}
//	
//	private static int nrOfGateways()
//	{
//		int g = 0, n = 1;
//		Stack<Integer> stack = new Stack<Integer>();
//		stack.push(n);
//		while (!stack.isEmpty())
//		{
//			n = stack.pop();
//			
//			// First, process the children:
//			if (l[n] != 0 && s[l[n]] == 0)
//			{
//				if (t[n])
//				{
//					s[n] = 'b';
//					continue;
//				}
//				stack.push(n);
//				if (l[n] != 0) stack.push(l[n]);
//				if (r[n] != 0) stack.push(r[n]);
//				continue;
//			}
//			
//			// Second, set the state of this node:
//			// no children
//			if (l[n] == 0)
//				s[n] = t[n] ? 'b' : 'c';
//			// one child
//			else if (r[n] == 0)
//				s[n] = t[n] ? 'b' : s[l[n]];
//			// two children
//			else if (t[n] || (s[l[n]] != 'c' && s[r[n]] != 'c'))
//				s[n] = 'b';
//			else if (s[l[n]] == 'c')
//				s[n] = s[r[n]] == 'c' ? 'c' : 'p';
//			else if (s[r[n]] == 'c')
//				s[n] = s[l[n]] == 'c' ? 'c' : 'p';
//			
//			// Third, count extra gateways:
//			// no children
//			if (l[n] == 0)
//				g += 0;
//			// one child
//			else if (r[n] == 0)
//				g += (t[n] && s[l[n]]!='b' ? 1 : 0);
//			// two children
//			else if (s[n] != 'b' || (s[l[n]] == 'b' && s[r[n]] == 'b'))
//				g += 0;
//			else if (s[l[n]] != 'b' && s[r[n]] != 'b')
//				g += 2;
//			else
//				g += 1;
//		}
//		if (s[1] != 'b')
//			g += 1;
//		return g;
//	}
//	
//	/**
//	 * Returns the string representation of this binary tree.
//	 */
//	private static String str(int root)
//	{
//		String[] tree = str2(root);
//		StringBuilder sb = new StringBuilder((1+tree[0].length())*tree.length);
//		for (String line : tree)
//		{
//			sb.append(line);
//			sb.append("\n");
//		}
//		return sb.toString();
//	}
//	
//	/**
//	 * Returns the string representation of the binary tree rooted at p.
//	 * The root is always added, because this method will not be called with a
//	 * null argument. Both children will be added if one or more of the children
//	 * are present. If only one child is present, the other child is represented
//	 * by an X. Note that the string representation of an element may NOT
//	 * contain a newline. It will not break if it does, but it will definitely
//	 * be screwed up.
//	 */
//	private static String[] str2(int node)
//	{
//		// the fill character (for debug purposes its useful to use ".")
//		String SPC = " ";
//		// the representation of the root element (dutch: wortel)
////		String w = "\""+node+"\"";
//		String w = "["+(char)(s[node]==0 ? ' ' : s[node])+"]"+node;
////		String ps = ""+p[node];
////		String ps = node==1 ? "" : t[node] ? "XXX" : "|";
//		String ps = t[node] ? "#"+p[node]+"#" : ""+p[node];
//		int psw = ps.length();
//		// that's all we have to do for leave nodes
//		if (l[node] == 0 && r[node] == 0)
//		{
//			int ww = w.length();
//			int width = Math.max(psw, w.length());
//			String[] s = new String[2];
//			s[0] = repeat(SPC, (width-psw)/2) + ps + repeat(SPC, (width-psw+1)/2);
//			s[1] = repeat(SPC, (width-ww)/2) + w + repeat(SPC, (width-ww+1)/2); 
//			return s;
//		}
//		
//		// represent non-existent children with "X"
//		String[] end = {"X"};
//		String[] L = l[node] != 0 ? str2(l[node]) : end;
//		String[] R = r[node] != 0 ? str2(r[node]) : end;
//		
//		// for convenience, make some width variables
//		int ww = w.length();
//		int lw = L[0].length();
//		int rw = R[0].length();
//		int sepw = 2;                   // the width of the separator
//		int lsrw = lw + sepw + rw;      // the width of the block left-sep-right
//		int width = Math.max(ww, lsrw); // the width of the final block
//		
//		String sep = repeat(SPC, sepw);
//		String[] s = new String[Math.max(L.length, R.length) + 4];
//		// to make a block of characters, fill some parts with spaces
//		String fillLftBlock = repeat(SPC, (width-lsrw)/2);   // left of children
//		String fillRgtBlock = repeat(SPC, (width-lsrw+1)/2); // rght of children
//		String fillLftBar   = repeat(SPC, (lw-1)/2);         // left of bar
//		String fillRgtBar   = repeat(SPC, (rw)/2);           // rght of bar
//		// zeroth, create the parent link
//		s[0] = repeat(SPC, (width-psw)/2) + ps + repeat(SPC, (width-psw+1)/2);
//		// first, create the root
//		s[1] = repeat(SPC, (width-ww)/2) + w + repeat(SPC, (width-ww+1)/2);
//		// then, create the bar (the branch)
//		s[2] = fillLftBlock +
//		       fillLftBar +
//		       repeat("_", (width-1)/2 - (width-lsrw)/2 - (lw-1)/2) +
//		       repeat("|", 1) +
//		       repeat("_", (width)/2 - (width-lsrw+1)/2 - (rw)/2) +
//		       fillRgtBar +
//		       fillRgtBlock;
//		// then, create the twigs for the leaves
//		s[3] = fillLftBlock +
//		       fillLftBar +
//		       repeat("|", 1) +
//		       repeat(SPC, lsrw - (lw-1)/2 - rw/2 - 2) +
//		       repeat("|", 1) +
//		       fillRgtBar +
//		       fillRgtBlock;
//		// finally, add the children
//		for (int i = 0, j = 4; j < s.length; i++, j++)
//		{
//			s[j] = fillLftBlock +
//			       ((i < L.length) ? L[i] : repeat(SPC, lw)) +
//			       sep +
//			       ((i < R.length) ? R[i] : repeat(SPC, rw)) +
//			       fillRgtBlock;
//		}
//		return s;
//	}
//	
//	/**
//	 * Repeats the given string n times, without any separator.
//	 */
//	private static String repeat(String s, int n)
//	{
//		StringBuilder sb = new StringBuilder(s.length()*n);
//		for (int i = 0; i < n; i++)
//			sb.append(s);
//		return sb.toString();
//	}
//	
}
