package bapc2006;

import java.io.File;
import java.util.Scanner;

class ProblemI
{
	
	/**
	 * Make it Manhattan. Solve by compressing the input to a D*D matrix. For
	 * the problem, it doesn't matter if a house is placed on coordinate (x, y)
	 * or (x+d, y), or (x+k*d, y+k*d) for that matter. Thus, count how many
	 * houses are built on the D*D grid starting in (0,0). A house is then built
	 * on (x%d, y%d). Take care of negative values by shifting the input domain
	 * from (-10^9, 10^9) to (0, 2*10^9) (i.e. adding 10^9 to each value).
	 * Then find the total number of houses on each row and each column. Now,
	 * the least houses must be demolished, when building the manhattanroads, on
	 * the coordinate where the sum of the row and column are the least. Take
	 * care that if you just do sumr[y]+sumc[x], you count (x,y) double.
	 * @param args
	 */
	public static void main(String[] args) throws Throwable
	{
		Scanner in = new Scanner(new File("bapc2006\\testdata\\i.in"));
		int cases = in.nextInt();
		while (cases-- > 0)
		{
			// read input
			int D = in.nextInt();
			int N = in.nextInt();
			int[] X = new int[D];
			int[] Y = new int[D];
			int[][] M = new int[D][D];
			
			// read and process input
			for (int i = 0; i < N; i++)
				M[(in.nextInt() + 1000000000) % D][(in.nextInt() + 1000000000) % D]++;
			
			// count houses on x and y-axis.
			int val;
			for (int x = 0; x < D; x++)
				for (int y = 0; y < D; y++)
				{
					X[x] += val = M[x][y];
					Y[y] += val;
				}
			
			// find the best x and y to place the crossroads on
			int minVal = Integer.MAX_VALUE;
			for (int x = 0; x < D; x++)
				for (int y = 0; y < D; y++)
					if ((val = X[x] + Y[y] - M[x][y]) < minVal)
						minVal = val;
			
			// and print the solution
			System.out.println(minVal);
		}
	}
	
}
