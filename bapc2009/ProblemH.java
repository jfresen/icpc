package bapc2009;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ProblemH
{
	public static void main(String[] args) throws Throwable
	{
		Scanner in = new Scanner(new File("bapc2009/testdata/h.in"));
		int cases = in.nextInt();
		while (cases-- > 0)
		{
			int nr = in.nextInt(), nc = in.nextInt();
			int er = in.nextInt(), ec = in.nextInt();
			int kr = in.nextInt(), kc = in.nextInt();
			int n = nr*nc, s = er*nc+ec, t = kr*nc+kc;
			List<Edge>[] edges = new List[n];
			for (int i = 0; i < n; i++)
					edges[i] = new ArrayList<Edge>();
			for (int i = 0; i < nr; i++)
				for (int j = 0, c; j < nc-1; j++)
					if ((c = in.nextInt()) != 0)
					{
						Edge e1 = new Edge(i*nc+j, i*nc+j+1, c+1);
						Edge e2 = new Edge(i*nc+j+1, i*nc+j, c+1);
						e1.dual = e2; e2.dual = e1;
						edges[e1.u].add(e1);
						edges[e2.u].add(e2);
					}
			for (int i = 0; i < nr-1; i++)
				for (int j = 0, c; j < nc; j++)
					if ((c = in.nextInt()) != 0)
					{
						Edge e1 = new Edge(i*nc+j, i*nc+j+nc, c+1);
						Edge e2 = new Edge(i*nc+j+nc, i*nc+j, c+1);
						e1.dual = e2; e2.dual = e1;
						edges[e1.u].add(e1);
						edges[e2.u].add(e2);
					}
			
			int[] pre = new int[n], que = new int[n], d = new int[n];
			while (true)
			{
				// do BFS to find a path
				Arrays.fill(pre, -1); pre[s] = s;
				int p=0, q=0, u, v, j;
				d[s] = Integer.MAX_VALUE;
				que[q++] = s;
				while (p < q && pre[t] < 0)
					for (Edge e : edges[u=que[p++]])
						if (pre[v=e.v] < 0 && (j=e.c-e.f) != 0)
						{
							pre[que[q++]=v] = u;
							d[v] = Math.min(d[u], j);
						}
				// if no path found, stop the algorithm
				if (pre[t] < 0) break;
				// apply the flow on the found path
				for (int i = t; i != s; i = pre[i])
					for (Edge e : edges[pre[i]])
						if (e.v == i)
						{
							e.f += d[t];
							e.dual.f -= d[t];
						}
			}
			// then, calculate the weight of the cut
			int size = 0;
			for (Edge e : edges[s])
				size += e.f;
			System.out.println(size * 1000);
		}
	}
	
	private static class Edge
	{
		int u, v, c, f;
		Edge dual; // the dual of this edge
		public Edge(int from, int to, int capacity)
		{u=from; v=to; c=capacity;}
		@Override
		public String toString()
		{return "("+u+","+v+") ["+f+"/"+c+"]";}
	}
}
