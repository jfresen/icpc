package dkp2006;

import java.io.BufferedReader;
import java.io.FileReader;

public class MakeItManhattan
{
	
	private static int D, N;
	private static int[][] grid;
	private static int[] rows;
	private static int[] cols;
	
	public static void main(String[] args) throws Exception
	{
		BufferedReader in = new BufferedReader(new FileReader("dkp2006\\testdata\\I.in"));
		for (int t = 0, T = Integer.parseInt(in.readLine()); t < T; t++)
			solve(in);
	}
	
	private static void solve(BufferedReader in) throws Exception
	{
		String[] input = in.readLine().split(" ");
		D = Integer.parseInt(input[0]);
		N = Integer.parseInt(input[1]);
		grid = new int[D][D];
		rows = new int[D];
		cols = new int[D];
		for (int i = 0, x, y; i < N; i++)
		{
			input = in.readLine().split(" ");
			x = (Integer.parseInt(input[0]) + 1000000000) % D;
			y = (Integer.parseInt(input[1]) + 1000000000) % D;
			grid[x][y]++;
			rows[y]++;
			cols[x]++;
		}
		int min = Integer.MAX_VALUE, sum;
		for (int x = 0; x < D; x++)
			for (int y = 0; y < D; y++)
				if (min > (sum = rows[y] + cols[x] - grid[x][y]))
					min = sum;
		System.out.println(min);
	}
	
}
