package ekp2004;

import java.io.*;
import java.util.*;

/* Sample Solution to C -- Taxi Cab scheme / Mikael Goldmann
 * Based on matching. Find largest possible matching of ends of trips
 * to beginnings of other trips.
 *
 * The answer is exactly the number of unmatched end of trips
 *
 * Start by greedily chosing a maximal matching rather 
 *  than starting from scratch. 
 */

class Trip
{
	int	x1;
	int	x2;
	int	y1;
	int	y2;
	int	time;

	Trip(int a1, int b1, int a2, int b2, int t)
	{
		x1 = a1;
		y1 = b1;
		x2 = a2;
		y2 = b2;
		time = t;
	}
}

class taxi_mg
{
	static BufferedReader	stdin	= new BufferedReader(new InputStreamReader(System.in));
	static PrintWriter		stdout	= new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)));
	int[]					vis;

	public static void main(String[] s) throws Exception
	{
		(new taxi_mg()).run();
		stdout.close();
	}

	void run() throws Exception
	{
		int N = Integer.parseInt(stdin.readLine());
		for (int i = 1; i <= N; ++i)
			solve(i);
	}

	void solve(int num) throws Exception
	{
		int N = Integer.parseInt(stdin.readLine());
		Trip[] trip = new Trip[N];

		String s1;
		StringTokenizer st1;
		int i, j;
		for (i = 0; i < N; ++i)
		{
			s1 = stdin.readLine();
			st1 = new StringTokenizer(s1);
			s1 = st1.nextToken(); // time to start
			int x1 = Integer.parseInt(st1.nextToken());
			int y1 = Integer.parseInt(st1.nextToken());
			int x2 = Integer.parseInt(st1.nextToken());
			int y2 = Integer.parseInt(st1.nextToken());
			st1 = new StringTokenizer(s1, ":");
			int time = Integer.parseInt(st1.nextToken());
			time *= 60;
			time += Integer.parseInt(st1.nextToken());
			trip[i] = new Trip(x1, y1, x2, y2, time);
		}

		boolean[] vmatched = new boolean[2 * N];
		int[][] adj = new int[2 * N][2 * N];

		for (i = 0; i < N; ++i)
			for (j = 0; j < N; ++j)
			{
				int x1 = trip[i].x1;
				int y1 = trip[i].y1;

				int x2 = trip[i].x2;
				int y2 = trip[i].y2;

				int x3 = trip[j].x1;
				int y3 = trip[j].y1;

				int t1 = trip[i].time + Math.abs(x1 - x2) + Math.abs(y1 - y2);

				t1 += Math.abs(x2 - x3) + Math.abs(y2 - y3);
				if (t1 < trip[j].time)
				{
					adj[i][j + N] = adj[j + N][i] = 1;
					if (!vmatched[i] && !vmatched[j + N])
					{
						adj[i][j + N] = adj[j + N][i] = -1;
						vmatched[i] = vmatched[j + N] = true;
					}

				}
			}

		int[] vis = new int[2 * N];
		int cookie = 1;

		while (improve(N, vmatched, adj, cookie, vis))
			++cookie;

		int sum = 0;
		for (i = N; i < 2 * N; ++i)
			if (!vmatched[i])
				++sum;
		stdout.println(sum);
	}

	boolean improve(int N, boolean[] vm, int[][] a, int cookie, int[] vis)
	{
		for (int i = 0; i < N; ++i)
			if (!vm[i] && vis[i] != cookie && dfs(i, i, N, vm, a, cookie, vis, 1))
				return true;
		return false;
	}

	boolean dfs(int s, int u, int N, boolean[] vm, int[][] adj, int cookie, int[] vis, int match)
	{
		vis[u] = cookie;
		if (!vm[u] && u != s)
		{ // found path! 
			vm[u] = true;
			return true;
		}
		int lo = (u >= N) ? 0 : N;
		int hi = (u >= N) ? N : 2 * N;
		lo = 0;
		hi = 2 * N;
		for (int i = lo; i < hi; ++i)
		{
			if (adj[u][i] == match && vis[i] != cookie)
			{
				if (dfs(s, i, N, vm, adj, cookie, vis, -match))
				{ // found path
					vm[u] = true;
					adj[u][i] = adj[i][u] = -match;
					return true;
				}
			}
		}
		return false;

	}

}
