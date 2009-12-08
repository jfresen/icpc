package ekp2004;

/* Sample solution to D - Pseudo random numbers / Mikael Goldmann
 * First count backwards to recreate the seed number 
 * The count forwards to get T:th number. Try using few columns to
 * save space.
 */
import java.io.*;

class random_mg
{
	static BufferedReader	stdin;
	int[][]					tab;

	public static void main(String[] ss) throws IOException
	{
		Reader rdr = new InputStreamReader(System.in);
		stdin = new BufferedReader(rdr);
		int n = Integer.parseInt(stdin.readLine());
		for (int i = 1; i <= n; ++i)
			(new random_mg()).solve(i);
	}

	void solve(int casenum) throws IOException
	{
		int i, j;

		int B = Integer.parseInt(stdin.readLine());
		String[] tok = stdin.readLine().split("[ ]+");
		int L = Integer.parseInt(tok[0]);
		int T = Integer.parseInt(stdin.readLine());
		int ncol = 5;

		int[] vals = new int[L];
		for (i = 1; i <= L; ++i)
			vals[i - 1] = Integer.parseInt(tok[i]);
		boolean ok = false;
		while (!ok)
		{
			ok = true;
			try
			{
				ncol *= 2;
				tab = newtab(L, ncol);
				for (i = 0; i < L; ++i)
					tab[i][0] = vals[i];
				for (i = L - 1; i > 0; --i)
					if (!explain(tab, i, ncol, B))
					{
						System.out.println("impossible");
						return;
					}
			}
			catch (ArrayIndexOutOfBoundsException e)
			{
				ok = false;
				// Retry, doubling number of columns
			}
		}
		// Now tab[0] contains the seed numbers digits

		int ncol1 = ncol + 1; // One extra column

		/* Try finding values on the T:th row
		 * If we fail and truncated any row, retry using
		 * twice as many columns
		 */
		int k;
		while (true)
		{
			boolean fullrow = false; // no overflowing row yet
			int[][] v = new int[2][ncol1];
			for (i = 0; i < ncol; ++i)
				v[0][i] = tab[0][i];
			for (i = ncol; i < ncol1; ++i)
				v[0][i] = -1;

			int[] a, b = null;
			;
			int tmp;
			ok = true;
			for (k = 1; k < T; ++k)
			{
				a = v[1 - (k & 1)];
				b = v[k & 1];
				for (i = 0; i < ncol1; ++i)
					b[i] = -1;

				for (i = 1, j = 0; j < ncol1 && i < ncol1; ++i)
				{
					if (a[i] == -1)
						break;

					tmp = a[i - 1] + a[i];
					if (tmp < B)
						b[j++] = tmp;
					else
					{
						b[j++] = tmp - B;
						if (j < ncol1)
							b[j++] = 1;
					}
				}
				if (j == ncol1)
					fullrow = true;
				if (b[0] == -1)
				{
					ok = false;
					break;
				}
			}
			if (ok)
			{
				System.out.println(b[0]);
				return;
			}
			else if (!fullrow)
			{
				System.out.println("unpredictable");
				return;
			}
			ncol1 *= 2;
		}
	}

	int[][] newtab(int r, int c)
	{
		int[][] t = new int[r][c];
		int i, j;
		for (i = 0; i < r; ++i)
			for (j = 0; j < c; ++j)
				t[i][j] = -1;
		return t;

	}

	/* explain() computes row (i-1) from row i
	 * Returns false if impossible. Used to calculate seed
	 * from the given sequence
	 */
	boolean explain(int[][] tab, int r, int ncol, int B)
	{
		int[] s = new int[ncol];
		int i, j;
		for (i = 0; i < ncol; ++i)
			s[i] = -1;
		i = 0;
		j = 1;
		int s1, t1;
		while (i < ncol && tab[r][i] != -1)
		{
			tab[r - 1][j] = (B + tab[r][i] - tab[r - 1][j - 1]) % B;
			s1 = tab[r - 1][j] + tab[r - 1][j - 1];
			++i;
			++j;

			if (s1 >= B)
			{
				if (tab[r][i] != -1 && tab[r][i] != 1)
					return false;
				++i;
			}
		}
		return true;

	}

}
