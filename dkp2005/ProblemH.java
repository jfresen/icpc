/*
 * Created on 3-okt-2005
 */
package dkp2005;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StreamTokenizer;

public class ProblemH
{

	public static int N, T, M;
	public static int[] t, m, v;
	public static int[][][] solution = new int[101][101][101];
	
	/**
	 * Venus Rover problem. Solve with dynamic programming with two dimensions
	 * instead of the usual one.
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException
	{
		// oldschool: back then we didn't have Scanner
		BufferedReader in = new BufferedReader(new FileReader("dkp2005\\sampledata\\h.in"));
		StreamTokenizer st = new StreamTokenizer(in);
		
		st.nextToken();
		int cases = (int)st.nval;
		
		for (int caseN = 0; caseN < cases; caseN++)
		{
			// read input
			st.nextToken();
			N = (int)st.nval;
			st.nextToken();
			T = (int)st.nval;
			st.nextToken();
			M = (int)st.nval;
			
			// init arrays. note: they are 1-based instead of 0-based.
			t = new int[N+1];
			m = new int[N+1];
			v = new int[N+1];
			
			// init cache for dynamic programming
			for (int i = 1; i <= N; i++)
				for (int j = 1; j <= T; j++)
					for (int k = 1; k <= M; k++)
						solution[i][j][k] = -1;
			
			// read more input
			for (int i = 1; i <= N; i++)
			{
				st.nextToken();
				t[i] = (int)st.nval;
				st.nextToken();
				m[i] = (int)st.nval;
				st.nextToken();
				v[i] = (int)st.nval;
			}
			
			// need i say any more?
			System.out.println(solve(N, T, M));
		}
	}
	
	// schoolvoorbeeld van backtracking met dynamisch programmeren :D
	public static int solve(int n, int tleft, int mleft)
	{
		// Niks meer over? dan is de solution 0
		if (n == 0 || tleft == 0 || mleft == 0)
			return 0;
		// Hebben we al een solution? return die dan
		if (solution[n][tleft][mleft] != -1)
			return solution[n][tleft][mleft];
		int pakWel = 0;
		int pakNiet = solve(n-1, tleft, mleft);   // Pak dit item niet
		if (t[n] < tleft && m[n] < mleft)         // Pak dit item wel
			pakWel = solve(n-1, tleft-t[n], mleft-m[n]) + v[n];
		// Sla de solution op
		solution[n][tleft][mleft] = Math.max(pakWel, pakNiet);
		return solution[n][tleft][mleft];
	}

}
