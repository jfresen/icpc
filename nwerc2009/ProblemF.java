package nwerc2009;

import java.io.File;
import java.util.Scanner;

public class ProblemF
{
	static int n, m, F;
	static int[] f;
	static Edge[][] tree;
	static int[] size;
	
	public static void main(String[] args) throws Throwable
	{
		Scanner in = new Scanner(new File("nwerc2009/testdata/f.in"));
		int cases = in.nextInt();
		while (cases-- > 0)
		{
			n = in.nextInt();
			tree = new Edge[n+1][1];
			size = new int[n+1];
			for (int i = 1; i < n; i++)
				add(new Edge(in.nextInt(), in.nextInt(), in.nextInt()));
			m = in.nextInt();
			f = new int[n+1];
			F = 0;
			for (int i = 0; i < m; i++)
				F += f[in.nextInt()] = in.nextInt();
			for (int i = 0; i < size[1]; i++)
				assignFrequencies(tree[1][i], 1);
			boolean costCalculated = false;
			for (int i = 1; i <= n; i++)
			{
				boolean isBest = true;
				Edge[] e = tree[i];
				for (int j = 0; j < size[i]; j++)
					isBest &= e[j].getF(i) >= e[j].getF(e[j].other(i));
				if (!isBest)
					continue;
				if (!costCalculated)
				{
					long cost = 0;
					for (int j = 0; j < size[i]; j++)
						cost += calculateCost(e[j], i);
					System.out.println(2*cost);
					costCalculated = true;
				}
				System.out.print(i + " ");
			}
			System.out.println();
		}
	}
	
	private static long calculateCost(Edge e, int a)
	{
		int b = e.other(a);
		long cost = (long)e.t*e.getF(b);
		for (int i = 0; i < size[b]; i++)
			if (tree[b][i] != e)
				cost += calculateCost(tree[b][i], b);
		return cost;
	}
	
	private static int assignFrequencies(Edge e, int a)
	{
		int b = e.other(a);
		int freq = f[b];
		for (int i = 0; i < size[b]; i++)
			if (tree[b][i] != e)
				freq += assignFrequencies(tree[b][i], b);
		e.setF(b, freq);
		e.setF(a, F-freq);
		return freq;
	}
	
	private static void add(Edge edge)
	{
		ensureCapacity(edge.a);
		ensureCapacity(edge.b);
		tree[edge.a][size[edge.a]++] = edge;
		tree[edge.b][size[edge.b]++] = edge;
	}
	
	private static void ensureCapacity(int i)
	{
		if (size[i] != tree[i].length)
			return;
		Edge[] tmp = new Edge[size[i]*2];
		System.arraycopy(tree[i], 0, tmp, 0, size[i]);
		tree[i] = tmp;
	}
	
	private static class Edge
	{
		int a, b, t;
		int fa, fb;
		public Edge(int a, int b, int t)
		{this.a=a; this.b=b; this.t=t;}
		public int other(int x)
		{return x==a ? b : a;}
		public void setF(int x, int f)
		{if (x==a) fa = f; else fb = f;}
		public int getF(int x)
		{return x==a ? fa : fb;}
	}
}
