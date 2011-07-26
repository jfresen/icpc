package nwerc2005;

import java.io.File;
import java.util.HashSet;
import java.util.Scanner;

public class ProblemF
{
	
	public static void main(String[] args) throws Throwable
	{
		Scanner in = new Scanner(new File("nwerc2005/testdata/f.in"));
		int cases = in.nextInt();
		while (cases-- > 0)
		{
			int g = in.nextInt(), m = g;
			int[] sid = new int[g];
			for (int i = 0; i < g; i++)
				sid[i] = in.nextInt();
			HashSet<Integer> used = new HashSet<Integer>();
			for (; used.size() != g; m++)
			{
				used.clear();
				for (int id : sid)
					if (!used.add(id%m))
						break;
			}
			System.out.println(--m);
		}
	}
	
}
