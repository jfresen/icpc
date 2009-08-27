package dkp2007;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;

public class ProblemA2
{
	
	public static int n = 0, l = 0;
	public static String[] words = new String[100];
	public static int[][] diff = new int[100][100];
	public static int[][] graph = new int[100][100];
	public static int[] degree = new int[100];
	public static int[] distance = new int[100];
	public static boolean[] inTree = new boolean[100];
	public static int[] parent = new int[100];
	
	/**
	 * @param args
	 * @throws Throwable 
	 */
	public static void main(String[] args) throws Throwable
	{
		BufferedReader in = new BufferedReader(new FileReader("dkp2007/testdata/A.in"));
		int tests = Integer.valueOf(in.readLine());
		while (tests-- > 0)
		{
			String[] nl = in.readLine().split(" ");
			n = Integer.valueOf(nl[0]);
			l = Integer.valueOf(nl[1]);
			int i, j, v, dist, length = n;
			String firstLadder = "", ladder = "";
			for (i = 0; i < n; i++)
				words[i] = in.readLine();
			Arrays.sort(words, 0, n);
			Arrays.fill(degree, 0, n, 0);
			for (i = 0; i < n; i++)
				for (j = i; j < n; j++)
					if (1 == (diff[i][j] = diff[j][i] = diff(words[i], words[j])))
					{
						graph[i][degree[i]++] = j;
						graph[j][degree[j]++] = i;
					}
			for (i = 0; i < n && length > l; i++)
			{
				// init intermediate dijkstra variables
				Arrays.fill(distance, 0, n, n);
				Arrays.fill(inTree, 0, n, false);
				Arrays.fill(parent, 0, n, -1);
				distance[i] = 0;
				v = i;
				// do dijkstra
				while (!inTree[v])
				{
					inTree[v] = true;
					// --- ADDED TO DIJKSTRA ---
					if (distance[v] > length)
						break;
					if (diff[i][v] >= l)
					{
						if (diff[i][v] < length)
						{
							length = diff[i][v];
							firstLadder = words[v];
							for (j = parent[v]; parent[j] != -1; j = parent[j])
								firstLadder = words[j] + " " + firstLadder;
							firstLadder = words[j] + " " + firstLadder;
						}
						else if (diff[i][v] == length)
						{
							ladder = words[v];
							for (j = parent[v]; parent[j] != -1; j = parent[j])
								ladder = words[j] + " " + ladder;
							ladder = words[j] + " " + ladder;
							if (firstLadder.compareTo(ladder) > 0)
								firstLadder = ladder;
						}
					}
					// -------------------------
					for (j = 0; j < degree[v]; j++)
						if (distance[graph[v][j]] > distance[v]+1)
						{
							distance[graph[v][j]] = distance[v]+1;
							parent[graph[v][j]] = v;
						}
					dist = n;
					for (j = 0; j < n; j++)
						if (!inTree[j] && dist > distance[j])
						{
							dist = distance[j];
							v = j;
						}
				}
			}
			System.out.println(firstLadder);
		}
	}
	
	public static int diff(String s, String t)
	{
		assert s.length() == t.length() : "Fool!";
		int n = s.length();
		char[] a = s.toCharArray();
		char[] b = new char[n<<1];
		System.arraycopy(a, 0, b, 0, n);
		System.arraycopy(t.toCharArray(), 0, b, n, n);
		Arrays.sort(a);
		Arrays.sort(b);
		int i = 0, j = 0, xtra = 0, mark = 0, count;
		while (i < n)
		{
			mark = i;
			while (++i < n && a[i-1] == a[i]);
			count = i - mark;
			mark = j;
			while (j < 2*n && b[j] < a[i-1]) j++;
			xtra += j - mark;
			mark = j + 2*count;
			while (j < 2*n && j < mark && b[j] == a[i-1]) j++;
		}
		return xtra + 2*n - j;
	}
	
}
