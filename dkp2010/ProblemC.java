package dkp2010;

import java.io.File;
import java.util.Scanner;

public class ProblemC
{
	
	public static void main(String[] args) throws Exception
	{
		Scanner in = new Scanner(new File("dkp2010/testdata/c.in"));
		int cases = in.nextInt();
		while (cases-- > 0)
		{
			int N = in.nextInt();
			int M = in.nextInt();
			int[] S = new int[N+1];
			int[][] cnt = new int[2][M];
			for (int i = 1; i <= N; i++)
				S[i] = in.nextInt()%M;
			
			for (int i = 1; i <= N; i++)
			{
				System.arraycopy(cnt[1], 0, cnt[0], 0, M);
				for (int j = 0, k = S[i]; j < M; j++, k = (k+1)%M)
					if (j == 0 || cnt[0][j] != 0)
						cnt[1][k] = max(cnt[0][k], cnt[0][j]+1);
			}
			
			System.out.println(cnt[1][0]);
		}
	}
	
	private static int max(int a, int b)
	{
		return a < b ? b : a;
	}
	
	public static String print(short[][] cnt, int[] S, int N, int M)
	{
		String s = "";
		for (int i = M; i-- > 0; )
		{
			s += String.format("%6d |", i);
			for (int j = 0; j <= N; j++)
				s += String.format(" %3d", cnt[j][i]);
			s += String.format("%n");
		}
		s += "-------+";
		for (int i = 0; i <= N; i++)
			s += "----";
		s += String.format("%n       |");
		for (int i = 1; i <= N; i += 2)
			s += String.format("  %6d", S[i]);
		s += String.format("%n       |    ");
		for (int i = 2; i <= N; i += 2)
			s += String.format("  %6d", S[i]);
		return s;
	}
	
}
