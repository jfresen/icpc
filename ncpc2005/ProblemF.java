package ncpc2005;

import java.io.File;
import java.util.Arrays;
import java.util.Scanner;

public class ProblemF
{
	public static void main(String[] args) throws Throwable
	{
		Scanner in = new Scanner(new File("ncpc2005/testdata/f.in"));
//		Scanner in = new Scanner(System.in);
		boolean first = true;
		while (true)
		{
			int n = in.nextInt();
			Year[] y = new Year[n];
			for (int i = 0; i < n; i++)
				y[i] = new Year(in.nextInt(), in.nextInt(), i-1);
			int m = in.nextInt();
			if (n==0 && m==0)
				return;
			if (first)
				first = false;
			else
				System.out.println();
			for (int i = 0; i < n; i++)
				while (y[i].prev != -1 && y[y[i].prev].r < y[i].r)
					y[i].prev = y[y[i].prev].prev;
			for (int i = 0; i < m; i++)
			{
				int Y = in.nextInt();
				int X = in.nextInt();
//				System.out.print("["+Y+", "+X+"]: ");
				int Yi = Arrays.binarySearch(y, new Year(Y));
				int Xi = Arrays.binarySearch(y, new Year(X));
				if (Yi < 0 && Xi < 0)
					System.out.println("maybe");
				else if (Yi < 0 && Xi >= 0)
					System.out.println(y[Xi].prev < ~Yi ? "maybe" : "false");
				else if (Yi >= 0 && Xi >= 0)
					System.out.println(y[Xi].prev != Yi ? "false" : (Xi-Yi == X-Y) ? "true" : "maybe");
				else if (Yi >= 0 && Xi < 0)
				{
					Xi = ~Xi - 1;
					while (y[Xi].r < y[Yi].r)
						Xi = y[Xi].prev;
					System.out.println(Xi > Yi ? "false" : "maybe");
				}
			}
		}
	}
	
	private static class Year implements Comparable<Year>
	{
		int y, r, prev;
		public Year(int y)               {this.y=y;}
		public Year(int y, int r, int p) {this.y=y; this.r=r; prev=p;}
		@Override public int compareTo(Year that)
		{return this.y - that.y;}
	}
}
