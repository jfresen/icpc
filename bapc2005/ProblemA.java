package bapc2005;

import java.io.File;
import java.util.Scanner;

public class ProblemA
{

	public static void main(String[] args) throws Throwable
	{
		Scanner in = new Scanner(new File("bapc2005/testdata/a.in"));
		int cases = in.nextInt();
		while (cases-- > 0)
		{
			// read input
			int Y = in.nextInt();
			int X = in.nextInt();
			int N = in.nextInt();
			int H = in.nextInt();
			in.nextLine();
			
			int[][] grid = new int[Y+2][X+2];
			for (int y = 1; y <= Y; y++)
			{
				String line = in.nextLine();
				for (int x = 1; x <= X; x++)
					grid[y][x] = line.charAt(x-1) - '0';
			}
			// read and preprocess input
			for (int i = 0; i < N; i++)
				grid[in.nextInt()][in.nextInt()]++;
			
			// process input
			boolean finished = false;
			while (!finished)
			{
				finished = true;
				for (int y = 1; y <= Y; y++)
					for (int x = 1; x <= X; x++)
						if (grid[y][x] > H)
						{
							finished = false;
							grid[y][x] -= 4;
							grid[y-1][x]++;
							grid[y][x+1]++;
							grid[y+1][x]++;
							grid[y][x-1]++;
						}
			}
			for (int y = 1; y <= Y; y++)
			{
				for (int x = 1; x <= X; x++)
					System.out.print(grid[y][x]);
				System.out.println();
			}
		}
	}

}
