package bapc2009;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

public class ProblemF
{
	// lookuptable for k, where k is defined as follows:
	// ( j + i*k[i][j] ) mod 11 == 0
	static int[][] K =
	{
		// note: column 0 will NEVER be searched in (because of 'trgt[i] != 0')
		{0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0},
		{0, 10,  9,  8,  7,  6,  5,  4,  3,  2,  1},
		{0,  5, 10,  4,  9,  3,  8,  2,  7,  1,  6},
		{0,  7,  3, 10,  6,  2,  9,  5,  1,  8,  4},
		{0,  8,  5,  2, 10,  7,  4,  1,  9,  6,  3},
		{0,  2,  4,  6,  8, 10,  1,  3,  5,  7,  9},
		{0,  9,  7,  5,  3,  1, 10,  8,  6,  4,  2},
		{0,  3,  6,  9,  1,  4,  7, 10,  2,  5,  8},
		{0,  4,  8,  1,  5,  9,  2,  6, 10,  3,  7},
		{0,  6,  1,  7,  2,  8,  3,  9,  4, 10,  5},
		{0,  1,  2,  3,  4,  5,  6,  7,  8,  9, 10},
	};
	public static void main(String[] args) throws Throwable
	{
		Scanner in = new Scanner(new File("bapc2009/testdata/f.in"));
		int cases = in.nextInt();
		while (cases-- > 0)
		{
			// read input
			int n = in.nextInt();
			int[][] warp = new int[n+1][12];
			for (int i = 0; i < 11; i++)
				warp[0][i] = in.nextInt();
			for (int i = 0; i < 11; i++)
				warp[0][i] = (in.nextInt()-warp[0][i]+11) % 11;
			for (int i = 1; i <= n; i++)
				for (int j = 0; j < 12; j++)
					warp[i][j] = in.nextInt();
			// sort warp trajectories on year of discovery
			Arrays.sort(warp, 1, n+1, new Comparator<int[]>(){
				@Override public int compare(int[] a, int[] b)
				{return a[11] - b[11];}});
			// guassian elemination
//			int year = 0, i, j, a = 0;
//			for (i = 0, j = 0; i < 11; i++, j = 0)
//				if (warp[0][i] != 0)
//				{
//					while (j < n && (a=K[warp[j][i]][warp[0][i]]) == 0) j++;
//					if (j == n)
//						break; // impossible
//					for (int l = i; l < 11; l++)
//					{
//						warp[0][l] = (warp[0][l] + a*warp[j][l]) % 11;
//						for (int m = 0; m < n; m++)
//							warp[m][l] = (warp[m][l] + a*warp[j][l]) % 11;
//					}
//					year = Math.max(year, warp[j][11]);
//				}
			int year = 0, i, j, k, l, a;
			for (i = 0; i < 11; i++)
			{
				for (j = 1; j <= n && warp[j][i] == 0; j++);
				if (warp[0][i] != 0 && j <= n)
					year = Math.max(year, warp[j][11]);
				else if (warp[0][i] != 0)
					break;
				else if (j > n)
					continue;
				for (k = 0; k <= n; k++)
				{
					if (k == j || warp[k][i] == 0)
						continue;
					a = K[warp[j][i]][warp[k][i]];
					for (l = i; l < 11; l++)
						warp[k][l] = (warp[k][l] + a*warp[j][l]) % 11;
				}
				if (j <= n)
					Arrays.fill(warp[j], i, 11, 0);
			}
			if (i != 11)
				System.out.println("unreachable");
			else
				System.out.println(year);
		}
	}
}
