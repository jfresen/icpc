package nwerc2004;

import java.io.*;

public class ProblemE
{

	public static void main(String args[]) throws IOException
	{
		Card2004[] adam = new Card2004[26];
		Card2004[] eva = new Card2004[26];

		BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer st = new StreamTokenizer(bf);
		st.eolIsSignificant(false);
		st.nextToken();
		int numCases = (int)st.nval;
		//	  System.out.println(numCases);
		for (int nCase = 0; nCase < numCases; nCase++)
		{
			st.nextToken();
			int k = (int)st.nval;
			//			System.out.println(k);
			int max = k;
			for (int j = 0; j < 2; j++)
			{
				for (int i = 0; i < k; i++)
				{
					st.nextToken();
					char c;
					int number, suit;
					String kaart;
					if (st.ttype == StreamTokenizer.TT_WORD)
					{
						kaart = st.sval;
						c = kaart.charAt(0);
						//						System.out.print(c);
						switch ((int)c)
						{
							case (int)'T':
								number = 10;
								break;
							case (int)'J':
								number = 11;
								break;
							case (int)'Q':
								number = 12;
								break;
							case (int)'K':
								number = 13;
								break;
							default:
								number = 14;
						}
						c = kaart.charAt(1);
						//						System.out.println(c);
						switch ((int)c)
						{
							case (int)'C':
								suit = 1;
								break;
							case (int)'D':
								suit = 2;
								break;
							case (int)'S':
								suit = 3;
								break;
							default:
								suit = 4;
						}
					}
					else
					{
						number = (int)st.nval;
						//						System.out.print(number);
						st.nextToken();
						c = st.sval.charAt(0);
						//						System.out.println(c);
						switch ((int)c)
						{
							case (int)'C':
								suit = 1;
								break;
							case (int)'D':
								suit = 2;
								break;
							case (int)'S':
								suit = 3;
								break;
							default:
								suit = 4;
						}
					}
					if (j == 0)
						adam[i] = new Card2004(number, suit);
					else
						eva[i] = new Card2004(number, suit);
				}
			}

			int numSorted = 0;
			int index;
			while (numSorted < k)
			{
				for (index = 1; index < k - numSorted; index++)
				{
					if (adam[index - 1].compareTo(adam[index]) < 0)
						swap(adam, index - 1, index);
				}
				numSorted++;
			}
			numSorted = 0;
			while (numSorted < k)
			{
				for (index = 1; index < k - numSorted; index++)
				{
					if (eva[index - 1].compareTo(eva[index]) < 0)
						swap(eva, index - 1, index);
				}
				numSorted++;
			}

			int adamFirst = 0, evaFirst = 0;
			while (k > 0)
			{
				//				System.out.println(max);
				if (adam[adamFirst].compareTo(eva[evaFirst]) > 0)
				{
					max--;
				}
				else
				{
					evaFirst++;
				}
				adamFirst++;
				k--;
			}
			System.out.println(max);

		}

	}

	public static void swap(Card2004[] data, int prev, int curr)
	{
		Card2004 temp = data[curr];
		data[curr] = data[prev];
		data[prev] = temp;
	}

}

class Card2004
{
	public Card2004(int a, int b)
	{
		n = a;
		s = b;
	}

	public int compareTo(Object other)
	{
		Card2004 that = (Card2004)other;
		if (n > that.n)
			return 1;
		else if (n == that.n)
		{
			if (s > that.s)
				return 1;
			else if (s > that.s)
				return 0;
			else
				return -1;
		}
		else
			return -1;
	}

	@Override
	public String toString()
	{
		String temp = "";
		switch (n)
		{
			case 10:
				temp += "T";
				break;
			case 11:
				temp += "J";
				break;
			case 12:
				temp += "Q";
				break;
			case 13:
				temp += "K";
				break;
			case 14:
				temp += "A";
				break;
			default:
				temp += n;
		}
		switch (s)
		{
			case 1:
				temp += "C";
				break;
			case 2:
				temp += "D";
				break;
			case 3:
				temp += "S";
				break;
			default:
				temp += "H";
		}
		return temp;
	}

	public int	n;
	public int	s;
}
