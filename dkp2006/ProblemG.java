package dkp2006;

import java.io.*;
import java.util.ArrayList;

class ProblemG {

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
			int wlen = word.length;
			int tlen = text.length;
			
			occ = 0;
			
			int wsum = 0, tsum = 0;
			
			for (int pos = 0; pos < wlen; pos++) {
				wsum += word[pos];
				tsum += text[pos];
			}
			
			//System.out.println("W: " + wsum);
			ArrayList poss = new ArrayList(100); 
			
			for (int pos = wlen; pos <= tlen; pos++) {
				//System.out.println("POS: " + pos + " W: " + wsum + " T: " + tsum);
				if (tsum == wsum) {
					poss.add(new Integer(pos));
				}
				// update checksum
				if (pos < tlen) {
					tsum = tsum - text[pos-wlen] + text[pos];
					//System.out.println("CHK - " + text[pos-wlen] + " + " + text[pos] + " S: " + tsum);
				}
			}
			
			for (int j = 0; j < poss.size(); j++) {
				if (check(((Integer) poss.get(j)).intValue() - wlen))
					occ++;
			}
			
			System.out.println(occ);
		}
	}
		
	public static boolean check(int pos) {
		for (int p = 0; p < word.length; p++) {
			if (text[pos+p] != word[p]) {
				//System.out.println("FA " + pos);
				return false;
			}
		}
		//System.out.println("OK " + pos);
		return true;
	}
	
	public static char[] word, text;
}
