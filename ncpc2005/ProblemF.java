//package ncpc2005;

import java.io.File;
import java.util.Arrays;
import java.util.Scanner;

public class ProblemF
{
	public static void main(String[] args) throws Throwable
	{
//		Scanner in = new Scanner(new File("ncpc2005/sampledata/f.in"));
		Scanner in = new Scanner(System.in);
		while (true)
		{
			int n = in.nextInt();
			Year[] y = new Year[n];
			for (int i = 0; i < n; i++)
				y[i] = new Year(in.nextInt(), in.nextInt());
			int m = in.nextInt();
			if (n==0 && m==0)
				return;
			for (int i = 1; i < n; i++)
			{
				y[i].prev = i-1;
				while (y[i].prev != -1 && y[y[i].prev].r < y[i].r)
					y[i].prev = y[y[i].prev].prev;
			}
			for (int i = 0; i < m; i++)
			{
				int Y = in.nextInt();
				int X = in.nextInt();
				int Yi = Arrays.binarySearch(y, new Year(Y));
				int Xi = Arrays.binarySearch(y, new Year(X));
				if (Yi < 0 && Xi < 0)
					System.out.println("maybe");
				else if (Yi < 0)
					System.out.println(y[Xi].prev != -1 && y[y[Xi].prev].y > Y ? "false" : "maybe");
				else
				{
					boolean full = (Xi > 0 && (X-Y == Xi-Yi));
					if (Xi < 0)
						Xi = ~Xi - 1;
					else
						Xi = y[Xi].prev;
					while (y[Xi].r < y[Yi].r)
						Xi = y[Xi].prev;
					System.out.println(Xi > Yi ? "false" : full ? "true" : "maybe");
				}
			}
			System.out.println();
		}
	}
	
	private static class Year implements Comparable<Year>
	{
		int y, r, prev = -1;
		public Year(int y)        {this.y=y;}
		public Year(int y, int r) {this.y=y; this.r=r;}
		@Override public int compareTo(Year that)
		{return this.y - that.y;}
	}
}
