package dkp2006;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;

public class ProblemI
{
	
	public static void main(String[] args) throws Throwable
	{
		Scanner in = new Scanner(new File("dkp2006\\testdata\\i.in"));
		int cases = in.nextInt();
		while (cases-- > 0)
		{
			// Read the input
			int N = in.nextInt();
			int M = in.nextInt();
			List<Edge>[] edges = new ArrayList[N+1];
			List<Edge>[] redges = new ArrayList[N+1];
			for (int i = 1; i <= N; i++)
			{
				edges[i] = new ArrayList<Edge>();
				redges[i] = new ArrayList<Edge>();
			}
			for (int i = 0; i < M; i++)
			{
				int a = in.nextInt(), b = in.nextInt(), w = in.nextInt();
				edges[a].add(new Edge(b, w));
				redges[b].add(new Edge(a, w));
			}
			int s = in.nextInt(), f = in.nextInt();
			
			// Do a reversed dijkstra for estimates of length from node i to f
			int[] len = new int[N+1];
			Arrays.fill(len, Integer.MAX_VALUE);
			PriorityQueue<Node> q = new PriorityQueue<Node>();
			q.add(new Node(f, 0));
			while (!q.isEmpty())
			{
				Node n = q.remove();
				if (len[n.i] > n.l)
					len[n.i] = n.l;
				else
					continue;
				for (Edge e : redges[n.i])
					q.add(new Node(e.t, n.l + e.l));
			}
			
			// Do a forward dijkstra with dynamic programming
			q = new PriorityQueue<Node>();
			int[][] dyn = new int[N+1][2];
			dyn[s][0] = 1;
			q.add(new Node(s, 0));
			while (!q.isEmpty())
			{
				Node n = q.remove();
				int i = n.l + len[n.i] - len[s];
				for (Edge e : edges[n.i])
				{
					int j = n.l + e.l + len[e.t] - len[s];
					if (j > 1)
						continue;
					if (dyn[e.t][j] == 0)
						q.add(new Node(e.t, n.l + e.l));
					dyn[e.t][j] += dyn[n.i][i];
				}
			}
			System.out.println(dyn[f][0] + dyn[f][1]);
		}
	}
	
	private static class Edge
	{
		public int t,l;
		public Edge(int t, int w)
		{this.t=t;this.l=w;}
	}
	
	private static class Node implements Comparable<Node>
	{
		public int i,l;
		public Node(int i, int l)
		{this.i=i;this.l=l;}
		public int compareTo(Node o)
		{return l - o.l;}
	}
	
}
