/*
 * Created on 22-sep-2005
 */
package ekp2004;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StreamTokenizer;

public class ProblemA
{

	static String[]	excludes	= new String[1000];
	static Object[]	queries		= new Object[100];
	static Object[]	results		= new Object[100];
	static int		excludesSize;
	static int		queriesSize;
	static int		number		= 0;

	static char[]	word		= new char[20];
	static int		wordSize	= 0;

	/**
	 * Een template om een probleem in te lezen.
	 * De comment is de regel die je gebruikt om te testen.
	 * 
	 * Hou er rekening mee dat het van wedstrijd tot wedstrijd kan verschillen hoe
	 * de data wordt aangeleverd (pipen naar de console, uit een bestand lezen, waar
	 * staat dat bestand?).
	 * 
	 * @param args
	 */
	public static void main(String[] args) throws IOException
	{

		BufferedReader bf = new BufferedReader(new FileReader("ekp2004\\sampledata\\a.in"));
		//		BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer st = new StreamTokenizer(bf);
		st.eolIsSignificant(false);
		st.nextToken();
		int numCases = (int)st.nval;
		for (int caseN = 0; caseN < numCases; caseN++)
		{
			// read the problem...
			st.nextToken();
			excludesSize = (int)st.nval;
			st.nextToken();
			queriesSize = (int)st.nval;
			for (int i = 0; i < excludesSize; i++)
			{
				st.nextToken();
				excludes[i] = st.sval;
			}
			for (int i = 0; i < queriesSize; i++)
			{
				st.nextToken();
				if (st.ttype == StreamTokenizer.TT_WORD)
					queries[i] = st.sval;
				else
					queries[i] = new Integer((int)st.nval);
				results[i] = null;
			}
			// problem read.
			// now solve it...
			int completedQueries = 0;
			String wordAsString;
			while (completedQueries < queriesSize)
			{
				nextWord();
				wordAsString = new String(word, 0, wordSize);
				if (isValidWord(wordAsString))
				{
					// our word is valid now
					number++;
					if (number % 1000000 == 0)
					{
						System.out.println(number);
					}
					boolean queryFound = false;
					for (int i = 0; i < queriesSize && !queryFound; i++)
					{
						if (results[i] != null)
						{
							continue;
						}
						Object query = queries[i];
						// is this query applicable on this word?
						// if so, store the answer of the query
						if (query instanceof String && query.equals(wordAsString))
						{
							results[i] = new Integer(number);
							completedQueries++;
							queryFound = true;
						}
						else if (query instanceof Integer && ((Integer)query).intValue() == number)
						{
							results[i] = wordAsString;
							completedQueries++;
							queryFound = true;
						}
					}
				}
			}
			for (int i = 0; i < queriesSize; i++)
			{
				System.out.println(results[i]);
			}
		}
	}

	public static void nextWord()
	{
		int charToAdept = wordSize - 1;
		boolean overflow = true;
		while (overflow && charToAdept >= 0)
		{
			overflow = word[charToAdept] == 'z';
			if (!overflow)
				word[charToAdept]++;
			else
				word[charToAdept] = 'a';
			charToAdept--;
		}
		if (!overflow)
			return;
		else
		{
			word[wordSize] = 'a';
			wordSize++;
		}
	}

	public static boolean isValidWord(String word)
	{
		for (int i = 0; i < excludesSize; i++)
		{
			if (word.indexOf(excludes[i]) != -1)
				return false;
		}
		return true;
	}

}
