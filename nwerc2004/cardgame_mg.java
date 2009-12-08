package nwerc2004;

/*
 * Greedy solution to card games based on..
 * Find the weakest card I have that beats his weakest card,
 * then repeat with his second weakest card, and so on.
 *
 * By Mikael Goldmann
 */

import java.io.*;
import java.util.*;

class cardgame_mg
{

	static BufferedReader	stdin;
	static String			face	= "23456789TJQKA";
	static String			suit	= "CDSH";

	public static void main(String[] args) throws IOException
	{
		Reader rdr = new InputStreamReader(System.in);
		stdin = new BufferedReader(rdr);
		int n = Integer.parseInt(stdin.readLine());
		for (int i = 0; i < n; ++i)
			(new cardgame_mg()).solve();
	}

	int[] readCards(int n) throws IOException
	{
		StringTokenizer sb = new StringTokenizer(stdin.readLine());
		int[] c = new int[n];
		for (int i = 0; i < n; ++i)
		{
			String card = sb.nextToken();
			int f = face.indexOf(card.charAt(0));
			int s = suit.indexOf(card.charAt(1));
			c[i] = f * 4 + s;
		}
		return c;
	}

	void solve() throws IOException
	{
		int ncards = Integer.parseInt(stdin.readLine());
		int[] his = readCards(ncards);
		int[] mine = readCards(ncards);
		Arrays.sort(his);
		Arrays.sort(mine);
		int pts = 0;
		int lo = 0, hi = ncards - 1;
		for (int i = 0; i < ncards; ++i)
		{
			if (mine[i] > his[lo])
			{
				++lo;
				++pts;
			}
		}
		System.out.println(pts);

	}
}
