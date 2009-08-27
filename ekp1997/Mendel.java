/*
 * Created on 24-jan-2006
 */ 
package ekp1997;

import java.io.BufferedReader;
import java.io.FileReader;

public class Mendel
{
	public static void main(String[] args) throws Throwable
	{
		BufferedReader in = new BufferedReader(new FileReader("ekp1997\\sampledata\\mendel.in"));
		int g = Integer.parseInt(in.readLine());
		for (; g > 0; g--)
		{
			System.out.println(1 - new Being(new Being(in.readLine()),new Being(in.readLine())).gg);
		}
	}
}

class Being
{
	public double pp, gp, gg;
	
	public Being(String hist)
	{
		switch (hist.charAt(0))
		{
			case 'P':
				pp = 1; gp = gg = 0;
				return;
			case 'G':
				pp = gp = 0; gg = 1;
				return;
			case ',':
				System.out.println("Histories shouldn't start with a comma, check your code");
				return;
		}
		int i = 1;
		int brackets = 0;
		while (!(brackets == 0 && hist.charAt(i) == ','))
		{
			if (hist.charAt(i) == '(')
				brackets++;
			else if (hist.charAt(i) == ')')
				brackets--;
			i++;
		}
		setParents(new Being(hist.substring(1, i)),
		           new Being(hist.substring(i+1, hist.length()-1)));
	}
	
	public Being(Being p1, Being p2)
	{
		setParents(p1, p2);
	}
	
	private void setParents(Being p1, Being p2)
	{
		// some terms are only needed once, others twice or more.
		// just calculate all terms to make it look better.
		double ad   = p1.pp * p2.pp,
		       ae_h = p1.pp * p2.gp * .5,
		       af   = p1.pp * p2.gg,
		       bd_h = p1.gp * p2.pp * .5,
		       be_q = p1.gp * p2.gp * .25,
		       bf_h = p1.gp * p2.gg * .5,
		       cd   = p1.gg * p2.pp,
		       ce_h = p1.gg * p2.gp * .5,
		       cf   = p1.gg * p2.gg;
		pp = ad + ae_h + bd_h + be_q;
		gp = ae_h + af + bd_h + be_q + be_q + bf_h + cd + ce_h;
		gg = cf + ce_h + bf_h + be_q;
	}
	
	@Override
	public String toString()
	{
		return "P(PP) = " + pp + "\n" +
		       "P(GP) = " + gp + "\n" +
		       "P(GG) = " + gg + "\n";
	}
}