package nwerc2009;

import java.io.File;
import java.util.Scanner;

public class ProblemC
{
	public static void main(String[] args) throws Throwable
	{
		Scanner in = new Scanner(new File("nwerc2009/testdata/c.in"));
		int cases = in.nextInt();
		while (cases-- > 0)
		{
			int	d = in.nextInt();
			int n = in.nextInt();
			int[] ints = new int[n];
			for (int i = 0; i < n; i++)
				ints[i] = in.nextInt();
			long[] mods = new long[d];
			mods[0]++;
			for (int i = 0, sum = 0; i < n; i++)
				mods[sum = (sum + ints[i]) % d]++;
			long num = 0;
			for (int i = 0; i < d; i++)
				num += (mods[i]*(mods[i]-1) / 2);
			System.out.println(num);
		}
	}
}
