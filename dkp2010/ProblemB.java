package dkp2010;

import java.io.File;
import java.util.Scanner;

public class ProblemB
{
	
	public static void main(String[] args) throws Exception
	{
		Scanner in = new Scanner(new File("dkp2010/testdata/b.in"));
		int cases = in.nextInt();
		while (cases-- > 0)
		{
			int D = in.nextInt();
			int M = in.nextInt();
			int Y = in.nextInt();
			if (equalOccurrences(""+D+M+Y) && isSplittable(D, M, Y))
				System.out.println("yes");
			else
				System.out.println("no");
		}
	}
	
	private static boolean equalOccurrences(String s)
	{
		byte[] occurrences = new byte[10];
		for (char c : s.toCharArray())
			occurrences[c-'0']++;
		for (int i = 0, j = -1; i < 10; i++)
			if (occurrences[i] == 0)
				continue;
			else if (j == -1)
				j = occurrences[i];
			else if (j != occurrences[i])
				return false;
		return true;
	}
	
	private static boolean isSplittable(int i1, int i2, int y)
	{
		int i3 = y/100;
		int i4 = y-i3*100;
		return (i1) == (i2+i3+i4)
			|| (i2) == (i1+i3+i4)
			|| (i3) == (i1+i2+i4)
			|| (i4) == (i1+i2+i3)
			|| (i1+i2) == (i3+i4)
			|| (i1+i3) == (i2+i4)
			|| (i1+i4) == (i2+i3);
	}
	
}
