package ekp2004;

import java.io.*;

public class word_jm
{
	public static int nextInt(StreamTokenizer tokens)
	{
		try
		{
			int ttype = tokens.nextToken();
			if (ttype != StreamTokenizer.TT_NUMBER)
				throw new IOException();
			return (int)(Math.round(tokens.nval));
		}
		catch (IOException e)
		{
			System.err.println("Error reading input");
		}
		return 0;
	}

	public static void main(String[] s)
	{
		Reader rdr = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer tokens = new StreamTokenizer(rdr);

		boolean i1[] = new boolean[27];
		boolean i2[][] = new boolean[27][27];
		boolean i3[][][] = new boolean[27][27][27];
		long nowords[][][] = new long[21][27][27];

		int N = nextInt(tokens);

		while (N-- > 0)
		{
			int nn = nextInt(tokens);
			int m = nextInt(tokens);

			for (int i = 0; i < 27; i++)
			{
				i1[i] = false;
				for (int j = 0; j < 27; j++)
				{
					i2[i][j] = false;
					for (int k = 0; k < 27; k++)
						i3[i][j][k] = false;
				}
			}

			int let[] = new int[3];

			for (int i = 0; i < nn; i++)
			{
				try
				{
					tokens.nextToken();
				}
				catch (IOException e)
				{
					System.err.println("Error reading input");
				}
				String word = tokens.sval;
				for (int j = 0; j < word.length(); j++)
					let[j] = (int)word.charAt(j) - 'a';
				switch (word.length())
				{
					case 1:
						i1[let[0]] = true;
						break;
					case 2:
						i2[let[0]][let[1]] = true;
						break;
					case 3:
						i3[let[0]][let[1]][let[2]] = true;
						break;
				}
			}

			for (int n = 0; n <= 20; n++)
				for (int x = 0; x <= 26; x++)
					for (int y = 0; y <= 26; y++)
					{
						long cnt = n > 0 ? 0 : 1;
						if (n > 0)
							for (int i = 0; i < 26; i++)
								if (!i1[i] && !i2[y][i] && !i3[x][y][i])
									cnt += nowords[n - 1][y][i];
						nowords[n][x][y] = cnt;
					}

			while (m-- > 0)
			{
				int ttype = 0;
				try
				{
					ttype = tokens.nextToken();
				}
				catch (IOException e)
				{
					System.err.println("Error reading input");
					return;
				}
				if (ttype == StreamTokenizer.TT_NUMBER)
				{
					int k = (int)(Math.round(tokens.nval)) - 1;
					int n = 1, x = 26, y = 26;
					String word = "";
					while (nowords[n][x][y] <= k)
						k -= nowords[n++][x][y];
					for (int j = 0; j < n; j++)
					{
						for (int i = 0; i < 26; i++)
						{
							if (!i1[i] && !i2[y][i] && !i3[x][y][i])
								if (nowords[n - j - 1][y][i] <= k)
									k -= nowords[n - j - 1][y][i];
								else
								{
									word = word + (char)('a' + i);
									x = y;
									y = i;
									break;
								}
						}
					}
					System.out.println(word);
				}
				else
				{
					String word = tokens.sval;
					int k = 1, x = 26, y = 26, n = word.length();
					for (int i = 1; i < n; i++)
						k += nowords[i][x][y];
					for (int i = 0; i < n; i++)
					{
						for (int j = 0; j < word.charAt(i) - 'a'; j++)
							if (!i1[j] && !i2[y][j] && !i3[x][y][j])
								k += nowords[n - i - 1][y][j];
						x = y;
						y = word.charAt(i) - 'a';
					}
					System.out.println(k);
				}
			}
		}
	}
}
