package dkp2007;

import java.io.File;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;

public class ProblemD
{
	
	public static int n, m;
	public static int[] h;
	public static int[][] w;
	
	public static void main(String[] args) throws Throwable
	{
		Scanner in = new Scanner(new File("dkp2007\\sampledata\\d.in"));
		int cases = in.nextInt();
		while (cases-- > 0)
		{
			// Read input
			n = in.nextInt();
			m = in.nextInt();
			h = new int[n+1];
			w = new int[n+1][n+1];
			for (int i = 1; i <= n; i++)
				h[i] = in.nextInt();
			for (int i = 0, s, t; i < m; i++)
				w[s=in.nextInt()][t=in.nextInt()] = w[t][s] = in.nextInt();
			
			// Do dijkstra
			Queue<Node> q = new PriorityQueue<Node>();
			q.add(new Node(1, h[1], h[1], 0, new int[4]));
			while (!q.isEmpty())
			{
				Node c = q.remove();
				if (c.to == n)
				{
					System.out.println(c.vd + " " + c.hd);
					break;
				}
				for (int i = 1; i <= n; i++)
					if (w[c.to][i] > 0 && !c.visited(i))
						q.add(new Node(i, c.min, c.max, c.hd + w[c.to][i], c.vis[0], c.vis[1], c.vis[2], c.vis[3]));
			}
		}
	}
	
	private static class Node implements Comparable<Node>
	{
		int to, min, max, hd, vd;
		int[] vis;
		public Node(int t, int mn, int mx, int d, int ... v)
		{to=t; hd=d; vis=v; vis[t>>5]|=(1<<(t&0x1F)); set(mn, mx);}
		private void set(int mn, int mx)
		{
			min = h[to] < mn ? h[to] : mn;
			max = h[to] > mx ? h[to] : mx;
			vd = max - min;
		}
		public boolean visited(int n)
		{
			return (vis[n>>5]&(1<<(n&0x1F))) != 0;
		}
		public int compareTo(Node o)
		{
			if (vd == o.vd)
				return hd - o.hd;
			return vd - o.vd;
		}
		@Override
		public String toString()
		{
			return String.format("%n%2d %5d %5d %3d %5d %016d", to, min, max, hd, vd, Integer.valueOf(Integer.toBinaryString(vis[0])));
		}
	}
	
}
