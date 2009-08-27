package dkp2006;

import java.io.File;
import java.util.Scanner;

public class ProblemB
{
	
	public static void main(String[] args) throws Exception
	{
		Scanner in = new Scanner(new File("dkp2006/testdata/b.in"));
		int cases = in.nextInt();
		while (cases-- > 0)
		{
			int a = in.nextInt(), b = in.nextInt(), c = in.nextInt(), d = in.nextInt();
			boolean[] l = new boolean[1010001];
			l[a] = l[b] = l[c] = l[d] = true;
			for (int i = 1; i < l.length; i++)
				l[i] |= l[f(i-a)] || l[f(i-b)] || l[f(i-c)] || l[f(i-d)];
			int sum = 0;
			int max = 0;
			for (int i = 1; i < l.length; i++)
				if (!l[i])
					if (i <= 1000000)
					{
						sum++;
						max = i;
					}
					else
						max = -1;
			System.out.println(sum);
			System.out.println(max);
		}
	}

	private static int f(int i)
	{
		return i > 0 ? i : 0;
	}
	
}
