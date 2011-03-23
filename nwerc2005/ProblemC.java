package nwerc2005;

import java.io.File;
import java.util.Scanner;

public class ProblemC
{
	
	static final int NORTH = 0;
	static final int EAST = 1;
	static final int SOUTH = 2;
	static final int WEST = 3;
	
	public static void main(String[] args) throws Throwable
	{
		Scanner in = new Scanner(new File("nwerc2005/testdata/c.in"));
		int cases = in.nextInt();
		while (cases-- > 0)
		{
			int n = in.nextInt();
			int r = in.nextInt();
			boolean[][] board = new boolean[n+1][n+1];
			for (int i = 0; i < r; i++)
				board[in.nextInt()][in.nextInt()] = true;
			int x = in.nextInt();
			int y = in.nextInt();
			int d = (x==0 ? EAST : x==n+1 ? WEST : y==0 ? NORTH : SOUTH);
			while (true)
			{
				x += (d==EAST ? 1 : d==WEST ? -1 : 0);
				y += (d==NORTH ? 1 : d==SOUTH ? -1 : 0);
				if (x == 0 || x == n+1 || y == 0 || y == n+1)
					break;
				if (board[x][y])
					d = (d+1)%4;
			}
			System.out.println(x + " " + y);
		}
	}
	
}
