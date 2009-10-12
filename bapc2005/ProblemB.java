package bapc2005;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ProblemB
{
	
	public static void main(String[] args) throws Throwable
	{
		Scanner in = new Scanner(new File("bapc2005/testdata/b.in"));
		int cases = in.nextInt();
		while (cases-- > 0)
		{
			int h = in.nextInt();
			List<Tuple> tuples = new ArrayList<Tuple>(2*h);
			for (int i = 0; i < h; i++)
			{
				double x = in.nextInt();
				double y = in.nextInt();
				int n = in.nextInt();
				if (y > 1000 || y < -1000)
					continue;
				double d = Math.sqrt(1e6 - y*y);
				tuples.add(new Tuple(x-d, n));
				tuples.add(new Tuple(x+d, -n));
			}
			Tuple[] crosspoints = tuples.toArray(new Tuple[0]);
			Arrays.sort(crosspoints);
			int max = 0;
			for (int i = 0, n = 0; i < crosspoints.length; i++)
				if (max < (n += crosspoints[i].n))
					max = n;
			System.out.println(max);
		}
	}
	
	public static class Tuple implements Comparable<Tuple>
	{
		public double x;
		public int n;
		public Tuple(double x, int v)
		{this.x = x;this.n = v;}
		@Override
		public int compareTo(Tuple that)
		{
			if (this.x < that.x)
				return -1;
			else if (this.x > that.x)
				return 1;
			else
				return that.n - this.n;
		}
	}
	
}
