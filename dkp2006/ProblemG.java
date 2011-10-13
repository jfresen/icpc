package dkp2006;

import java.io.File;
import java.util.Scanner;

public class ProblemG
{
	public static void main(String[] args) throws Throwable
	{
		Scanner in = new Scanner(new File("dkp2006/testdata/g.in"));
		int cases = in.nextInt();
		while (cases-- > 0)
			System.out.println(knuthMorrisPratt(in.next(), in.next()));
	}

	private static int knuthMorrisPratt(String needle, String haystack)
	{
		char[] t = needle.toCharArray();
		char[] s = haystack.toCharArray();
		int i, j, T = t.length, S = s.length, c = 0; // c = match count
		int[] x = new int[T+1];
		
		// Building of the Partial Match Table
		for (i = 1, j = 0; i < T;)
			if (t[i] == t[j])
				x[++i] = ++j;
			else if (j > 0)
				j = x[j];
			else
				i++;
		
		// Find all matches
		for (i = 0, j = 0; i < S;)
			if (s[i] == t[j])
				{i++; if (++j == T) {c++; j = x[j];}}
			else if (j > 0)
				j = x[j];
			else
				i++;
		
		return c;
	}
}
