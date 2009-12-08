package nwerc2004;

/*
 Java solution of problem Invest
 for NWEPC2004
 author: P.G.Kluit
 date  : May/June 2004
 revised: Oktober 2004, IO-checker added
 */

import java.io.*;

public class InvestMain
{
	private static String	infile	= "invest.txt";

	public static void main(String[] args)
	{
		try
		{
			if (args.length > 0) // another inputfile can be given
				infile = args[0]; // as command line argument
			InputStream is = System.in;
			if (args.length > 0)
				is = new FileInputStream(infile);
			IntReader ir = new IntReader(is);
			int aantal = ir.read();
			for (int k = 0; k < aantal; k++)
			{
				InvestProblem p = InvestProblem.read(ir);
				p.solve();
				System.out.println(p.getSolution());
			}
		}
		catch (IOException iox)
		{
			System.err.println(iox.toString());
		}
	}
}

class InvestProblem
{

	private int			amount;
	private int			years;
	private Debentures	debentures;

	public InvestProblem(int amount, int years, Debentures debentures)
	{
		this.amount = amount;
		this.years = years;
		this.debentures = debentures;
	}

	public static void check(boolean condition, String s)
	{
		if (!condition)
			System.out.println(s);
	}

	public static InvestProblem read(IntReader ir)
	{
		int amount = ir.read();
		check(amount <= 1000000, "amount should be at most 1.000.000, but is " + amount);
		check(amount > 0, "amount should be > 0, but is " + amount);
		int years = ir.read();
		check(years <= 40, "years should be at most 40, but is " + years);
		check(years > 0, "years should be > 0, but is " + years);

		Debentures debentures = new Debentures(ir);
		return new InvestProblem(amount, years, debentures);
	}

	public void solve()
	{
		for (int k = 0; k < years; k++)
		{
			amount = debentures.bestResult(amount);
		}
	}

	public int getSolution()
	{
		return amount;
	}
}

class Debentures
{
	private Debenture[]	debenture;
	private int			noOfDebentures;

	private int[]		rendement	= new int[45260];
	private int			next		= 0;

	public Debentures(IntReader ir)
	{
		noOfDebentures = ir.read();
		InvestProblem.check(noOfDebentures <= 10, "Number of bonds should be at most 10, but is " + noOfDebentures);
		InvestProblem.check(noOfDebentures > 0, "Number of bonds should be positive, but is " + noOfDebentures);
		debenture = new Debenture[noOfDebentures];
		for (int k = 0; k < noOfDebentures; k++)
			debenture[k] = Debenture.read(ir);
	}

	public int bestResult(int amount)
	{
		return amount + getRendement(amount);
	}

	private int getRendement(int amount)
	{
		int index = amount / 1000;
		for (; next < index; next++)
			for (int k = 0; k < noOfDebentures; k++)
				update(next + debenture[k].kvalue, rendement[next] + debenture[k].interest);
		return rendement[index];
	}

	private void update(int val, int rend)
	{
		if (rendement[val] < rend)
			rendement[val] = rend;
	}

}

class Debenture
{
	int	kvalue;		// in 1000
	int	interest;

	public Debenture(int value, int interest)
	{
		kvalue = value / 1000;
		this.interest = interest;
	}

	public static Debenture read(IntReader ir)
	{
		int amount = ir.read();
		InvestProblem.check(amount % 1000 == 0, "value of bond should be a multiple of 1000, but is " + amount);
		InvestProblem.check(amount > 0, "value of bond should be positive, but is " + amount);
		int interest = ir.read();
		InvestProblem.check(10 * interest <= amount, "interest should be no more than, amount / 10 but amount = " + amount + " and interest = " + interest);
		InvestProblem.check(interest > 0, "interest should be positive, but is " + interest);
		return new Debenture(amount, interest);
	}

}

class IntReader extends StreamTokenizer
{

	public IntReader(InputStream is)
	{
		super(new BufferedReader(new InputStreamReader(is)));
	}

	public int read()
	{
		int tokentype;
		try
		{
			do
			{ //skip over non-numbers
				tokentype = nextToken();
			} while (tokentype != TT_NUMBER);
		}
		catch (IOException iox)
		{
			System.out.println(iox);
		}
		return (int)nval;
	}
}
