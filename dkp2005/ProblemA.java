// Solution uses Dijkstra with dynamic programming.
// graph:    double[][] as adjacency matrix, -1 means unconnected
// dijkstra: create a Node object with pathlength for each potential node to visit

package dkp2005;

import java.io.File;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Scanner;

public class ProblemA
{

	public static void main(String[] args) throws Throwable
	{
		new ProblemA();
	}
	
	public ProblemA() throws Throwable
	{
		Scanner in = new Scanner(new File("dkp2005/testdata/a.in"));
		int cases = in.nextInt();
		while (cases-- > 0)
		{
			// Read the input
			int n = in.nextInt();
			int w = in.nextInt();
			int m = in.nextInt();
			int s = in.nextInt();
			int t = in.nextInt();
			double[][] p = new double[n+1][n+1];
			for (int i = 0; i < n; i++)
				Arrays.fill(p[i], -1);
			for (int i = 0; i < w; i++)
			{
				int s1 = in.nextInt();
				int s2 = in.nextInt();
				p[s1][s2] = p[s2][s1] = 1-in.nextDouble();
			}
			
			// Dijkstra with dynamic programming
			double[][] cache = new double[n+1][m+1];
			PriorityQueue<Node> queue = new PriorityQueue<Node>();
			queue.add(new Node(s, 1, 0));
			Node c = null;
			while (!queue.isEmpty())
			{
				c = queue.poll();
				// Reached our destination
				if (c.n == t)
					break;
				// Already passed this point with better probability
				if (cache[c.n][c.m] >= c.p)
					continue;
				cache[c.n][c.m] = c.p;
				for (int i = 1; i <= n; i++)
					if (p[c.n][i] != -1)
					{
						queue.add(new Node(i, c.p*p[c.n][i], c.m));
						if (c.m < m)
							queue.add(new Node(i, c.p*(1-.5*(1-p[c.n][i])), c.m+1));
					}
			}
			System.out.printf("%.4f%n", 1-c.p);
		}
	}
	
	private class Node implements Comparable<Node>
	{
		public int n;
		public double p;
		public int m;
		public Node(int n, double p, int m)
		{this.n=n;this.p=p;this.m=m;}
		
		public int compareTo(Node o)
		{
			if (p > o.p)
				return -1;
			else
				return 1;
		}
	}

}