package nkp2001;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StreamTokenizer;

public class Template
{

	public static void main(String[] args) throws IOException
	{
		BufferedReader reader = new BufferedReader(new FileReader("sampledata\\x.in"));
//		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer st = new StreamTokenizer(reader);
		st.eolIsSignificant(false);
		st.nextToken();
		int numCases = (int)st.nval;
		for (int caseN = 0; caseN < numCases; caseN++)
		{
		}
	}

}

