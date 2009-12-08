package nwerc2002;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Locale;

public class ProblemB
{
	
	public static final int MAX = 'L' - 'A' + 1;
	public static Train[][][] graph;
	public static int[] route;
	public static boolean[] inRoute;
	public static Train goal;
	public static int[] maxRoute;
	public static double maxP;
	
	public static void main(String[] args) throws Throwable
	{
		BufferedReader in = new BufferedReader(new FileReader("ekp2002/testdata/b.in"));
		int cases = stoi(in.readLine());
		while (cases-- > 0)
		{
			graph = new Train[MAX][MAX][];
			route = new int[MAX];
			inRoute = new boolean[MAX];
			maxRoute = new int[MAX];
			maxP = 0;
			int n = stoi(in.readLine());
			Train[] t = new Train[n];
			for (int i = 0; i < n; i++)
			{
				String[] s = in.readLine().split("[ :]");
				t[i] = new Train(s[0].charAt(0)-'A',
				                      s[3].charAt(0)-'A',
				                      stoi(s[1])*60 + stoi(s[2]),
				                      stoi(s[4])*60 + stoi(s[5]),
				                      stod(s[6]));
			}
			String[] s = in.readLine().split("[ :]");
			goal = new Train(s[0].charAt(0)-'A',
			                 s[3].charAt(0)-'A',
			                 stoi(s[1])*60 + stoi(s[2]),
			                 stoi(s[4])*60 + stoi(s[5]),
			                 1);
			for (int i = 0; i < n; i++)
				if (t[i].to != goal.to)
					t[i].arr++; // extra minute for changing trains
			Arrays.sort(t);
			for (int i = 0, j = i; i < n; i = j)
			{
				while (j < n && t[i].from==t[j].from && t[i].to==t[j].to) j++;
				graph[t[i].from][t[i].to] = new Train[j-i];
				System.arraycopy(t, i, graph[t[i].from][t[i].to], 0, j-i);
			}
			route[0] = goal.from;
			inRoute[goal.from] = true;
			solve(0);
			for (int i = 0; maxRoute[i] != goal.to; i++)
				System.out.print((char)(maxRoute[i]+'A') + " ");
			System.out.println((char)(goal.to+'A'));
			System.out.printf(Locale.ENGLISH, "%.4f%n", maxP);
		}
	}
	
	public static void solve(int i)
	{
		if (route[i] == goal.to)
		{
			double p = probability(0, goal.dep, 1);
			if (maxP < p)
			{
				maxP = p;
				System.arraycopy(route, 0, maxRoute, 0, i+1);
			}
		}
		else
			for (int j = 0; j < MAX; j++)
				if (!inRoute[j] && graph[route[i]][j] != null)
				{
					inRoute[j] = true;
					route[i+1] = j;
					solve(i+1);
					inRoute[j] = false;
				}
	}
	
	public static double probability(int i, int time, double p)
	{
		// reached destination
		if (route[i] == goal.to)
			return time <= goal.arr ? p : 0;
		// time's up
		if (time >= goal.arr)
			return 0;
		double q = 0;
		int f = route[i], t = route[i+1];
		for (int j = 0; j < graph[f][t].length; j++)
		{
			if (time <= graph[f][t][j].dep)
			{
				q += probability(i+1, graph[f][t][j].arr, p*(1-graph[f][t][j].p));
				p = p*graph[f][t][j].p;
			}
		}
		return q;
	}
	
	public static int stoi(String s)
	{return Integer.valueOf(s);}
	public static double stod(String s)
	{return Double.valueOf(s);}
	
}

class Train implements Comparable<Train>
{
	public int from, to, dep, arr;
	public double p;
	public Train(int from, int to, int dep, int arr, double p)
	{this.from = from; this.to = to; this.dep = dep; this.arr = arr; this.p = p;}
	public int compareTo(Train t)
	{return ((from<<26)+(to<<22)+(dep<<11)+arr)-((t.from<<26)+(t.to<<22)+(t.dep<<11)+t.arr);}
	@Override
	public String toString()
	{return s(from) + " " + t(dep) + " " + s(to) + " " + t(arr) + " " + p;}
	public static String t(int t)
	{return (t<600?"0":"") + t/60 + ":" + (t%60<10?"0":"") + t%60;}
	public static String s(int t)
	{return ""+(char)(t+'A');}
}
