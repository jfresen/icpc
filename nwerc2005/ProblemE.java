package nwerc2005;

import java.io.File;
import java.util.Scanner;

public class ProblemE
{
	
	static int n, m;
	static int[] p, l, r; // parent, leftChild and rightChild arrays
	static int[] t; // traps
	
	public static void main(String[] args) throws Throwable
	{
		Scanner in = new Scanner(new File("nwerc2005/sampledata/e.in"));
		while ((n=in.nextInt()) != 0)
		{
			m = in.nextInt();
			p = new int[n+1];
			l = new int[n+1];
			r = new int[n+1];
			for (int i = 2; i <= n; i++)
			{
				int x = in.nextInt();
				if (x > 0) l[i] = in.nextInt();
				if (x > 1) r[i] = in.nextInt();
				if (x > 2) p[i] = in.nextInt();
			}
			setLinks(l[1], 1);
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
	
}
