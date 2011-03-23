package nwerc2005;

import java.io.File;
import java.util.Scanner;

public class ProblemD
{
	
	static int n;
	static int[] h, r, R;  // height, lower radius, upper radius
	static double[] b;     // bottom
	static boolean[] used;
	
	public static void main(String[] args) throws Throwable
	{
		Scanner in = new Scanner(new File("nwerc2005/testdata/d.in"));
		int cases = in.nextInt();
		while (cases-- > 0)
		{
			n = in.nextInt();
			h = new int[n];
			r = new int[n];
			R = new int[n];
			b = new double[n];
			used = new boolean[n];
			for (int i = 0; i < n; i++)
			{
				h[i] = in.nextInt();
				r[i] = in.nextInt();
				R[i] = in.nextInt();
			}
			System.out.println((int)min(0));
		}
	}
	
	private static double min(int i)
	{
		if (i == n)
			return height();
		double minheight = Double.MAX_VALUE;
		for (int j = 0; j < n; j++)
			if (!used[j])
			{
				b[j] = bottom(j);
				used[j] = true;
				minheight = Math.min(minheight, min(i+1));
				used[j] = false;
			}
		return minheight;
	}
	
	private static double height()
	{
		// Given b[j], j=0..n-1, calculate the height of the stack.
		double height = b[0] + h[0];
		for (int i = 1; i < n; i++)
			if (height < b[i] + h[i])
				height = b[i] + h[i];
		return height;
	}
	
	private static double bottom(int i)
	{
		double bottom = 0;
		for (int j = 0; j < n; j++)
			if (used[j] && bottom < bottom(j, i))
				bottom = bottom(j, i);
		return bottom;
	}
	
	private static double bottom(int l, int u)
	{
		double ml = (double)h[l]/(R[l]-r[l]);
		double mu = (double)h[u]/(R[u]-r[u]);
		// upper stands on top of lower
		if (r[u] >= R[l])
			return b[l] + h[l];
		// upper hangs in brim of lower
		if (ml >= mu && R[u] >= R[l])
			return Math.max(b[l], b[l] + h[l] - mu*(R[l]-r[u]));
		// upper hangs against the side of lower if lower is deep enough
		if (ml >= mu)
			return Math.max(b[l], b[l] - h[u] + ml*(R[u]-r[l]));
		// upper stands against the side of lower if lower is deep enough
		return Math.max(b[l], b[l] + ml*(r[u]-r[l]));
	}
	
}
