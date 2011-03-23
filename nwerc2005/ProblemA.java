package nwerc2005;

import java.io.File;
import java.util.Scanner;

public class ProblemA
{
	
	static final int N = 1000;
	
	static int n, q;
	static int[] w;
	static long[] p;
	static boolean[] done;
	static long[][] dyn = new long[6][N];
	
	public static void main(String[] args) throws Throwable
	{
		Scanner in = new Scanner(new File("nwerc2005/sampledata/a.in"));
		while ((n=in.nextInt()) != 0)
		{
			w = new int[n+1];
			for (int i = 1; i <= n; i++)
				w[i] = in.nextInt();
			p = new long[q=in.nextInt()];
			done = new boolean[q];
			for (int i = 0; i < q; i++)
				p[i] = in.nextLong();
			for (int i = 1; i <= n; i++)
				dyn[i][0] = 1;
			for (int W = 1; W < N; W++)
				for (int i = 1; i <= n; i++)
					dyn[i][W] = dyn[i-1][W] + (w[i]<=W ? dyn[i][W-w[i]] : 0);
			System.out.printf("    |");
			for (int i = 1; i <= n; i++)
				System.out.printf("%7d", w[i]);
			System.out.println();
			for (int j = 0; j < N; j++)
			{
				if (j%8 == 0)
					System.out.println("----+-----------------------------------");
				else if (j%4 == 0)
					System.out.println("- - + - - - - - - - - - - - - - - - - - ");
				System.out.printf("%3d |", j);
				for (int i = 1; i <= n; i++)
					System.out.printf("%7d", dyn[i][j]);
				System.out.println();
			}
		}
	}

//	private static boolean check(int W)
//	{
//		boolean b = true;
//		for (int i = 0; i < q; i++)
//			if (!done[i])
//				if (p[i] <= dyn[n-1][W])
//					done[i] = true;
//				else
//					b = false;
//	}

//	private static boolean done()
//	{
//		for (int i = 0; i < q; i++)
//			if (!done[i])
//				return false;
//		return true;
//	}
	
}
