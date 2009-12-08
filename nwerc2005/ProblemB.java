package nwerc2005;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;
import java.util.StringTokenizer;

public class ProblemB
{

	static Hashtable<String, Integer> ingrToNum;
	static Hashtable<Integer, String> numToProd;
	
	static int P, Q;
	static int maxFromLastFixed, lastFixed;
	static int sumMin = 0;
	
	static Ingredient[][] ingrCalc;
	static Ingredient[][] ingr;
	static Ingredient[] solutions = new Ingredient[10];
	
	static BufferedReader in;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception
	{
		ingrToNum = new Hashtable<String, Integer>();
		numToProd = new Hashtable<Integer, String>();
		in = new BufferedReader(new FileReader("ekp2005\\sampledata\\declaration.in"));
//		in = new BufferedReader(new InputStreamReader(System.in));
		P = Integer.parseInt(in.readLine());
		
		while (P != 0)
		{
			readTest();
			processInput();
			executeQueries();
			P = Integer.parseInt(in.readLine());
		}
		
	}
	
	public static void readTest() throws IOException
	{
		ingrCalc = new Ingredient[P+1][];
		ingr = new Ingredient[P+1][1001];
		for (int productI = 1; productI <= P; productI++)
		{
			String prodName = in.readLine();
			numToProd.put(productI, prodName);
			int n = Integer.parseInt(in.readLine());
			ingrCalc[productI] = new Ingredient[n+1];
			for (int ingredientI = 1; ingredientI <= n; ingredientI++)
			{
				StringTokenizer input = new StringTokenizer(in.readLine());
				String ingrName = input.nextToken();
				Ingredient ing = null;
				Integer ingrNum = ingrToNum.get(ingrName);
				if (ingrNum == null)
				{
					ing = new Ingredient(productI, ingrToNum.size());
					ingrToNum.put(ingrName, ing.ingr);
				}
				else
				{
					ing = new Ingredient(productI, ingrNum);
				}
				if (input.hasMoreTokens())
				{
					String percentage = input.nextToken();
					ing.min = ing.max = Integer.parseInt(percentage.substring(0,percentage.length()-1));
				}
				ingr[productI][ing.ingr] = ing;
				ingrCalc[productI][ingredientI] = ing;
			}
		}
	}
	
	public static void processInput()
	{
		for (int i = 1; i <= P; i++)
		{
			calc(i);
		}
	}
	
	public static void calc(int px)
	{
		maxFromLastFixed = 0;
		sumMin = 0;
		Ingredient[] ingrInProduct = ingrCalc[px];
		int n = ingrInProduct.length - 1;
		lastFixed = n+1;
		for (int i = n; i > 0; i--)
		{
			if (ingrInProduct[i].min != 0)
			{
				int min = ingrInProduct[i].min;
				int sum = 0;
				for (int j = lastFixed - 1; j >= i; j--)
				{
					ingrInProduct[j].max = min;
					sum += min;
				}
				maxFromLastFixed += sum;
				lastFixed = i;
			}
			else
			{
				if (i == n)
				{
					ingrInProduct[i].min = 1;
				}
				else if (i == 1)
				{
					double min = (100.0 - maxFromLastFixed) / (lastFixed - 1);
					if (min - Math.floor(min) != 0.0)
						min++;
					ingrInProduct[i].min = (int)Math.floor(min);
				}
				else
				{
					ingrInProduct[i].min = ingrInProduct[i+1].min;
				}
				ingrInProduct[i].max = (int)((100.0 - sumMin) / i);
			}
			sumMin += ingrInProduct[i].min;
		}
	}
	
	public static void executeQueries() throws IOException
	{
		Q = Integer.parseInt(in.readLine());
		for (int query = 0; query < Q; query++)
		{
			StringTokenizer input = new StringTokenizer(in.readLine());
			String leastMost = input.nextToken();
			String ingrName = input.nextToken();
			Integer ingrNumObject = ingrToNum.get(ingrName);
			if (ingrNumObject == null)
			{
				for (int i = 1; i <= P; i++)
				{
					System.out.print(numToProd.get(i) + " ");
				}
				System.out.println();
				break;
			}
			int ingrNum = ingrNumObject;
			int numSolutions = 0;
			for (int i = 1; i <= P; i++)
			{
				if (ingr[i][ingrNum] != null)
				{
					solutions[numSolutions] = ingr[i][ingrNum];
					numSolutions++;
				}
			}
			if (leastMost.equals("least"))
			{
				for (int i = 0; i < numSolutions; i++)
				{
					for (int j = i+1; !solutions[i].dropped && j < numSolutions; j++)
					{
						solutions[i].filterMin(solutions[j]);
					}
				}
			}
			else // most
			{
				for (int i = 0; i < numSolutions; i++)
				{
					for (int j = i+1; !solutions[i].dropped && j < numSolutions; j++)
					{
						solutions[i].filterMax(solutions[j]);
					}
				}
			}
			for (int i = 0; i < numSolutions; i++)
			{
				if (!solutions[i].dropped)
				{
					System.out.print(numToProd.get(solutions[i].prod) + " ");
				}
			}
			System.out.println();
		}
	}

}

class Ingredient
{
	
	public Ingredient(int prod, int ingr)
	{
		this.prod = prod;
		this.ingr = ingr;
	}
	
	int min = 0, max = 0, prod, ingr;
	boolean dropped = false;
	
	public void filterMin(Ingredient that)
	{
		if (this.max < that.min)
			that.dropped = true;
		else if (this.min > that.max)
			this.dropped = true;
	}
	
	public void filterMax(Ingredient that)
	{
		if (this.max < that.min)
			this.dropped = true;
		else if (this.min > that.max)
			that.dropped = true;
	}
	
	@Override
	public String toString()
	{
		return ingr + ": " + min + " " + max;
	}

}
