package ncpc2005;

import java.io.File;
import java.util.Arrays;
import java.util.Scanner;

public class ProblemI
{
	public static void main(String[] args) throws Throwable
	{
		Scanner in = new Scanner(new File("ncpc2005/sampledata/i.in"));
//		Scanner in = new Scanner(System.in);
		nextcase: while (true)
		{
			int k = in.nextInt();
			if (k == 0)
				return;
			double[] a = new double[k];
			for (int i = 0; i < k; i++)
				a[i] = in.nextDouble();
			Arrays.sort(a);
			double c = a[0];
			for (int i = 1; i < k; i++)
			{
				if (c >= a[i])
				{
					System.out.println("YES");
					continue nextcase;
				}
				c += a[i];
			}
			System.out.println("NO");
		}
	}
}
