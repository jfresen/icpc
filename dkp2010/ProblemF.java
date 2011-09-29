package dkp2010;

import java.io.File;
import java.util.Scanner;

public class ProblemF
{
	public static void main(String[] args) throws Exception
	{
		Scanner in = new Scanner(new File("dkp2010/testdata/f.in"));
		int cases = in.nextInt();
		while (cases-- > 0)
		{
			int n = in.nextInt();
			int[] p = new int[n+1];
			for (int i = 1; i <= n; i++)
				p[i] = p[i-1] + in.nextInt();
			int buy=0, sel=1, b=0, s=1;
			for (int i = 2; i <= n; i++)
			{
				if (p[b] > p[i-1])
				{
					if ((p[sel]-p[buy]) < (p[s]-p[b]))
					{
						buy = b;
						sel = s;
					}
					b = i-1;
					s = i;
				}
				if (p[s] < p[i])
					s = i;
			}
			if ((p[sel]-p[buy]) < (p[s]-p[b]))
			{
				buy = b;
				sel = s;
			}
			System.out.println((buy+1) + " " + sel);
		}
	}
}
