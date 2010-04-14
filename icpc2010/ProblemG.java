package icpc2010;

import java.io.File;
import java.util.Scanner;

public class ProblemG
{
	
	public static int n, b1, b2;
	public static int[] x = new int[100];
	public static int[] y = new int[100];
	public static double[][] d = new double[100][100];
	
	public static void main(String[] args) throws Throwable
	{
		Scanner in = new Scanner(new File("icpc2010/sampledata/g.in"));
		for (int cases = 1;; cases++)
		{
			n = in.nextInt();
			b1 = in.nextInt();
			b2 = in.nextInt();
			if (n == 0 && b1 == 0 && b2 == 0)
				return;
//			x = new int[n];
//			y = new int[n];
//			d = new double[n][n];
			for (int i = 0; i < n; i++)
			{
				x[i] = in.nextInt();
				y[i] = in.nextInt();
				for (int j = 0; j < i; j++)
					d[i][j] = d[j][i] = Math.sqrt((x[i]-x[j])*(x[i]-x[j]) + (y[i]-y[j])*(y[i]-y[j]));
			}
			double[][] M = new double[n][n];
			fill(M, b1, b2);
			double d1 = M[n-1][n-1];
			String s1 = traceBack(M);
			fill(M, b2, b1);
			double d2 = M[n-1][n-1];
			String s2 = traceBack(M);
			if (d1 < d2)
				System.out.printf("Case %d: %.2f\n%s\n", cases, d1, s1);
			else
				System.out.printf("Case %d: %.2f\n%s\n", cases, d2, s2);
		}
	}
	
	private static void fill(double[][] M, int bi, int bj)
	{
		M[0][0] = 0;
		for (int i = 0; i < n; i++) // i = heen, j = terug
			for (int j = 0; j < n; j++)
			{
				// Skip the origin:
				if (i == 0 && j == 0)
					continue;
				// Mark all infeasible solutions:
				if (i == j || j == 1 || i == bj || j == bi || i > bj && j < bj || j > bi && i < bi)
					M[i][j] = Double.POSITIVE_INFINITY;
				// Visit island j and j-1 in the 2nd pass:
				else if (i < j-1)
					M[i][j] = M[i][j-1] + d[j-1][j];
				// Visit island j in the 2nd pass, and j-1 in the 1st pass:
				else if (i == j-1)
				{
					// Check all possible last visited islands in the 2nd pass:
					double min = Double.POSITIVE_INFINITY;
					for (int k = 0; k < j; k++)
						if (min > M[i][k] + d[k][j])
							min = M[i][k] + d[k][j];
					M[i][j] = min;
				}
				// Visit island i and i-1 in the 1st pass:
				else if (j < i-1)
					M[i][j] = M[i-1][j] + d[i-1][i];
				// Visit island i in the 1st pass, and j=i-1 in the 2nd pass:
				else if (j == i-1)
				{
					// Check all possible last visited islands in the 1st pass:
					double min = Double.POSITIVE_INFINITY;
					for (int k = 0; k < i; k++)
						if (min > M[k][j] + d[k][i])
							min = M[k][j] + d[k][i];
					M[i][j] = min;
				}
			}
		double min = Double.POSITIVE_INFINITY;
		for (int i = 0; i < n-1; i++)
			if (min > M[n-1][i] + d[i][n-1])
				min = M[n-1][i] + d[i][n-1];
		M[n-1][n-1] = min;
	}
	
	private static String traceBack(double[][] M)
	{
		int i = 0, j = 0;
		String fwd = "0", bwd = "0";
		for (int k = 1; k < n; k++)
			if (M[i][j] + d[j][k] == M[i][k])
				bwd = (j=k) + " " + bwd;
			else
				fwd = fwd + " " + (i=k);
		return fwd + " " + bwd;
	}
	
	public static String printM(double[][] M)
	{
		String s = "";
		for (int i = n-1; i >= 0; i--)
		{
			s += format(M[i][0]);
			for (int j = 1; j < n; j++)
				s += " " + format(M[i][j]);
			s += "\n";
		}
		return s;
	}
	
	private static String format(double d)
	{
		if (d == Double.POSITIVE_INFINITY)
			return " inf";
		return String.format("%4.1f", d);
	}
	
}

