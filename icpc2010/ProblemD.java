package icpc2010;

import java.io.File;
import java.util.Scanner;

public class ProblemD
{
	
	public static int n;
	public static int[] need, lose;
	public static int[] send, back;
	public static int[][] edges;
	public static int[] degree;
	
	public static void main(String[] args) throws Throwable
	{
		Scanner in = new Scanner(new File("icpc2010/sampledata/d.in"));
		for (int cases = 1;; cases++)
		{
			n = in.nextInt();
			if (n == 0)
				return;
			need = new int[n+1];
			lose = new int[n+1];
			edges = new int[n+1][n+1];
			degree = new int[n+1];
			for (int i = 1; i <= n; i++)
			{
				need[i] = in.nextInt();
				lose[i] = in.nextInt() + in.nextInt();
			}
			for (int i = 1; i < n; i++)
			{
				int u = in.nextInt(), v = in.nextInt();
				edges[u][degree[u]++] = v;
				edges[v][degree[v]++] = u;
			}
			int min = Integer.MAX_VALUE;
//			for (int i = 1, a; i <= n; i++)
//				if (min > (a = needed(0, i)))
//					min = a;
		}
	}
	
	/**
	 * We need to send a certain number of soldiers to each edge, but on each
	 * edge we also get some of them back.
	 * @param fr
	 * @param to
	 */
	private static void needed(int fr, int to)
	{
		send[to] = Math.max(need[to], lose[to]);
		for (int nxt = 0; nxt < degree[to]; nxt++)
			if (nxt != fr)
			{
				// how much do we send on each edge?
				needed(to, nxt);
				send[to] += send[nxt];
				
			}
	}
	
}
