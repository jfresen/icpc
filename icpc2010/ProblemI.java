package icpc2010;

import java.io.File;
import java.util.Random;
import java.util.Scanner;

public class ProblemI
{
	
	public static int X, Y, XY;
	public static int x1, y1, s1, x2, y2, s2, x3, y3, s3, x4, y4, s4;
	
	public static boolean[][] seen;
	public static int step;
	
	public static void main(String[] args) throws Throwable
	{
		Scanner in = new Scanner(new File("icpc2010/sampledata/i.in"));
		for (int cases = 1;; cases++)
		{
//			long start = System.currentTimeMillis();
			Y = in.nextInt();
			X = in.nextInt();
			if (X == 0 && Y == 0)
				return;
			y1 = in.nextInt(); x1 = in.nextInt(); s1 = 1*X*Y/4;
			y2 = in.nextInt(); x2 = in.nextInt(); s2 = 2*X*Y/4;
			y3 = in.nextInt(); x3 = in.nextInt(); s3 = 3*X*Y/4;
			y4 = 0;            x4 = 1;            s4 = 4*X*Y/4;
			
			step = 0;
			seen = new boolean[X][Y];
			System.out.println("Case "+cases+": "+tours(1,0,0));
//			long end = System.currentTimeMillis();
//			System.out.printf("Time %d: %.4f\n", cases, (end-start)/1000.0);
		}
	}
	
	private static long tours(int s, int x, int y)
	{
		// If out of bounds or already been there:
		if (x < 0 || x == X || y < 0 || y == Y || seen[x][y])
			return 0;
		// We should be at a checkpoint, or we arrive at the wrong time:
		if (s == s1 && !(x == x1 && y == y1) || x == x1 && y == y1 && s != s1 ||
		    s == s2 && !(x == x2 && y == y2) || x == x2 && y == y2 && s != s2 ||
		    s == s3 && !(x == x3 && y == y3) || x == x3 && y == y3 && s != s3 ||
		    s == s4 && !(x == x4 && y == y4) || x == x4 && y == y4 && s != s4)
			return 0;
		// If we travelled correctly over the entire grid, we found 1 tour:
		if (s == s4)
			return 1;
		// If there's no more chance of finding a valid route, return 0:
		if (!stillPossible(s, x, y))
			return 0;
		
		long n = 0;
		seen[x][y] = true;
		n += tours(s+1, x+1, y);
		n += tours(s+1, x-1, y);
		n += tours(s+1, x, y+1);
		n += tours(s+1, x, y-1);
		seen[x][y] = false;
		
		return n;
	}
	
	private static boolean stillPossible(int s, int x, int y)
	{
		// Possible checks:
		// - Flood-fill from (x,y) and check if all positions filled
		// - Manhattan distance to next checkpoint ≤ remaining steps
		// - Find dead ends
		
		// Check if manhattan distance to next step is smaller then or equal to
		// the remaining number of steps until that step:
		if (s < s1 && (Math.abs(x1-x)+Math.abs(y1-y)) > (s1-s))
			return false;
		if (s < s2 && (Math.abs(x2-x)+Math.abs(y2-y)) > (s2-s))
			return false;
		if (s < s3 && (Math.abs(x3-x)+Math.abs(y3-y)) > (s3-s))
			return false;
		if (s < s4 && (Math.abs(x4-x)+Math.abs(y4-y)) > (s4-s))
			return false;
		
		// Check if we can still reach all positions on the grid:
		boolean[][] filled = new boolean[X][Y];
		for (int i = 0; i < X; i++)
			System.arraycopy(seen[i], 0, filled[i], 0, Y);
		fill(filled, x, y);
		for (int i = 0; i < X; i++)
			for (int j = 0; j < Y; j++)
				if (!filled[i][j])
					return false;
		
		// Check if there are any dead ends:
		for (int i = 0; i < X; i++)
			for (int j = 0; j < Y; j++)
				if (!(i==x && j==y) && !(i==1 && j==0) && isDeadEnd(i, j))
					return false;
		
		return true;
	}
	
	private static void fill(boolean[][] filled, int x, int y)
	{
		filled[x][y] = true;
		if (x+1 <  X && !filled[x+1][y]) fill(filled, x+1, y);
		if (x-1 >= 0 && !filled[x-1][y]) fill(filled, x-1, y);
		if (y+1 <  Y && !filled[x][y+1]) fill(filled, x, y+1);
		if (y-1 >= 0 && !filled[x][y-1]) fill(filled, x, y-1);
	}
	
	private static boolean isDeadEnd(int x, int y)
	{
		if (seen[x][y])
			return false;
		int neighbours = 0;
		if (x+1 <  X && !seen[x+1][y]) neighbours++;
		if (x-1 >= 0 && !seen[x-1][y]) neighbours++;
		if (y+1 <  Y && !seen[x][y+1]) neighbours++;
		if (y-1 >= 0 && !seen[x][y-1]) neighbours++;
		return neighbours <= 1;
	}
	
	private static void generate8x8()
	{
		Random r = new Random();
		System.out.println("8 8");
		System.out.println((0+r.nextInt(8)) + " " + (0+r.nextInt(3)) + " " +
		                   (4+r.nextInt(4)) + " " + (3+r.nextInt(5)) + " " +
		                   (0+r.nextInt(4)) + " " + (3+r.nextInt(5)));
	}
	
}
