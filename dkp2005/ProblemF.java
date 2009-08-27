package dkp2005;

import java.io.File;
import java.util.Scanner;

public class ProblemF
{

	public static int UNASSIGNED = 0;
	public static int WHITE = 1;
	public static int RED = 2;
	public static int BLUE = 3;
	public static int YELLOW = 4;
	
	public static int n;
	public static int[] x1;
	public static int[] x2;
	public static int[] y1;
	public static int[] y2;
	public static boolean[][] linked;
	public static int[] color;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Throwable
	{
		Scanner in = new Scanner(new File("dkp2005/testdata/f.in"));
		int cases = in.nextInt();
		while (cases-- > 0)
		{
			// Read input
			n = in.nextInt();
			x1 = new int[n]; // upper right
			y1 = new int[n]; //  ,,
			x2 = new int[n]; // lower left
			y2 = new int[n]; //  ,,
			for (int i = 0 ; i < n; i++)
			{
				int u1 = in.nextInt(), v1 = in.nextInt();
				int u2 = in.nextInt(), v2 = in.nextInt();
				x1[i] = Math.min(u1, u2); // ^
				x2[i] = Math.max(u1, u2); // |
				y1[i] = Math.max(v1, v2); // y
				y2[i] = Math.min(v1, v2); //   x -->
			}
			
			// Build adjacency matrix
			linked = new boolean[n][n];
			for (int i = 0; i < n; i++)
				for (int j = 0; j < i; j++)
					if (isAdjacent(i,j))
						linked[i][j] = linked[j][i] = true;
			
			// Backtrack and count
			color = new int[n];
			System.out.println(backtrack(0));
		}
	}

	private static int backtrack(int i)
	{
		if (i == n)
			return 1;
		int possibilities = 0;
		for (color[i] = YELLOW; color[i] != UNASSIGNED; color[i]--)
			if (isValid(i))
				possibilities += backtrack(i+1);
		return possibilities;
	}

	private static boolean isValid(int i)
	{
		if (color[i] == WHITE)
			return true;
		for (int j = 0; j < i; j++)
			if (linked[i][j] && color[i] == color[j])
				return false;
		return true;
	}

	private static boolean isAdjacent(int i, int j)
	{
		return ((x1[i] == x2[j] || x2[i] == x1[j]) && y1[i] > y2[j] && y2[i] < y1[j]) ||
		       ((y1[i] == y2[j] || y2[i] == y1[j]) && x1[i] < x2[j] && x2[i] > x1[j]);
	}

}
