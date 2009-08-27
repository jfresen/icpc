package ekp2004;

/* Sample solution to Watchdog
 * Author: Mikael Goldmann
 *
 * Try all possible points of attachment
 */

import java.io.*;
import java.util.*;

class dog_mg
{
	static BufferedReader	stdin;
	static PrintStream		stdout;

	public static void main(String[] a) throws Exception
	{
		Reader rdr = new InputStreamReader(System.in);
		stdin = new BufferedReader(rdr);
		OutputStream os = new BufferedOutputStream(System.out);
		stdout = new PrintStream(os);

		(new dog_mg()).run();

		stdout.close();
		if (stdin.readLine() != null)
			System.err.println("*** Stuff after last test case ***");
	}

	void run() throws Exception
	{
		int n = 0;
		try
		{
			n = Integer.parseInt(stdin.readLine());
		}
		catch (Exception e)
		{
			System.err.println("*** Cannot read number of cases ***");
			throw e;
		}
		for (int i = 1; i <= n; ++i)
		{
			try
			{
				solve(i);
			}
			catch (Exception e)
			{
				System.err.println("***Problem in case " + i + " ***");
				throw e;
			}
		}
	}

	void solve(int cnt) throws Exception
	{
		String s = stdin.readLine();
		StringTokenizer st = new StringTokenizer(s);
		int S = Integer.parseInt(st.nextToken());
		int H = Integer.parseInt(st.nextToken());
		int i;
		int[] hx = new int[H];
		int[] hy = new int[H];
		for (i = 0; i < H; ++i)
		{
			s = stdin.readLine();
			st = new StringTokenizer(s);
			hx[i] = Integer.parseInt(st.nextToken());
			hy[i] = Integer.parseInt(st.nextToken());
		}
		int x, y;
		int best = 10000;
		for (x = 1; x < S; ++x)
			for (y = 1; y < S; ++y)
			{
				boolean onHatch = false;
				for (i = 0; !onHatch && i < H; ++i)
					onHatch = (x == hx[i] && y == hy[i]);
				if (!onHatch)
				{
					int leash = Math.min(Math.min(x, y), Math.min(S - x, S - y));
					boolean all = true;
					for (i = 0; all && i < H; ++i)
					{
						int dx = x - hx[i];
						int dy = y - hy[i];
						all = dx * dx + dy * dy <= leash * leash;
					}
					if (all)
					{ // Solution found!
						stdout.println(x + " " + y);
						return;
					}
				}
			}
		stdout.println("poodle");
	}
}
