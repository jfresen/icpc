package dkp2006;

import java.io.*;

class ProblemG2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		BufferedReader r = new BufferedReader(new FileReader("g.in"));
		int cases = Integer.parseInt(r.readLine());
		int occ, begin, found;
		for (int i = 0; i < cases; i++)
		{
			word = r.readLine().toCharArray();
			text = r.readLine().toCharArray();
			// String word = r.readLine();
			// String text = r.readLine();
			int wlen = word.length;
			int tlen = text.length;
			
			occ = 0;
			for (int pos = 0; pos < tlen - wlen + 1; pos++) {
				occ++;
				for (int wp = word.length - 1; wp >= 0; wp--) {
					//System.out.println("" + text[pos+wp] + " " + word[wp]);
					if (text[pos+wp] != word[wp]) {
						occ--;
						break;
					}
				}
			}
			/*begin = 0;
			do
			{
				found = text.indexOf(word, begin);
				if(found > -1)
				{
					begin = found +1;
					occ++;
				}
			}
			while(found > -1);*/
			
			System.out.println(occ);
		}
	}
	
	public static char[] word, text;
}
