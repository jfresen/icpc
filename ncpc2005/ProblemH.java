package ncpc2005;

import java.io.File;
import java.util.Scanner;

public class ProblemH
{
	public static void main(String[] args) throws Throwable
	{
		Scanner in = new Scanner(new File("ncpc2005/sampledata/h.in"));
//		Scanner in = new Scanner(System.in);
		int cases = in.nextInt();
		while (cases-- > 0)
		{
			String s = in.next();
			for (int i = 0, S = s.length(), j; i < S; i = j)
				for (j = i+1; j <= S; j++)
					if (j == S || endOfNecklace(s, i, j))
					{
						System.out.print("("+s.substring(i,j)+")");
						break;
					}
			System.out.println();
		}
	}

	private static boolean endOfNecklace(String s, int i, int j)
	{
		String ti = s.substring(i,j);
		String tj = s.substring(j);
		if (tj.compareTo(ti) >= 0)
			return false;
		for (j = 1; j <= tj.length() && ti.startsWith(tj.substring(0,j)); j++)
			if (ti.matches("("+tj.substring(0,j)+")*"))
				return false;
		return true;
	}
}
