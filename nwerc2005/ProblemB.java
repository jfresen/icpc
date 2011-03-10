package nwerc2005;

import java.io.File;
import java.util.Scanner;

public class ProblemB
{
	
	static int P, Q;
	static Product[] product;
	
	public static void main(String[] args) throws Throwable
	{
		Scanner in = new Scanner(new File("nwerc2005/testdata/b.in"));
		while ((P=in.nextInt()) != 0)
		{
			product = new Product[P];
			for (int i = 0; i < P; i++)
				product[i] = new Product(in);
			Q = in.nextInt();
			for (int i = 0; i < Q; i++)
				executeQuery(in);
		}
	}
	
	private static void executeQuery(Scanner in)
	{
		String type = in.next();
		String ingredient = in.next();
		int l = 0, u = 100;
		for (int i = 0, j; i < P; i++)
		{
			j = product[i].find(ingredient);
			l = Math.max(l, j != -1 ? product[i].lb[j] : 0);
			u = Math.min(u, j != -1 ? product[i].ub[j] : 0);
		}
		String ans = "";
		if (type.equals("most"))
			for (int i = 0, j; i < P; i++)
				if (((j=product[i].find(ingredient)) != -1 ? product[i].ub[j] : 0) >= l)
					ans += product[i].name+" ";
		if (type.equals("least"))
			for (int i = 0, j; i < P; i++)
				if (((j=product[i].find(ingredient)) != -1 ? product[i].lb[j] : 0) <= u)
					ans += product[i].name+" ";
		System.out.println(ans.substring(0, ans.length()-1));
	}
	
	private static class Product
	{
		int n;
		String name;
		String[] ingredient;
		int[] lb, ub;
		
		public Product(Scanner in)
		{
			name = in.next();
			n = in.nextInt();
			lb = new int[n];
			ub = new int[n];
			ingredient = new String[n];
			for (int i = n-1; i >= 0; i--)
			{ // (store ingredients in increasing order)
				ingredient[i] = in.next();
				String s = in.nextLine();
				if ("".equals(s))
					lb[i] = (ub[i] = 100) - 99;
				else
					lb[i] = ub[i] = Integer.parseInt(s.substring(1, s.length()-1));
			}
			tightenBounds();
		}
		
		private void tightenBounds()
		{
			for (int i = 0, a = 0; i < n; i++)
				a = lb[i] = Math.max(lb[i], a);
			for (int i = n-1, a = 100; i >= 0; i--)
				a = ub[i] = Math.min(ub[i], a);
			boolean changed = true;
			while (changed)
			{
				changed = false;
				for (int i = 0; i < n; i++)
				{
					int l = 0, u = 0, j; // lower, upper, j
					for (j = i+1; j < n; j++)                      u += ub[j];
					for (j =   0; j < i && ub[j] < lb(u,i,j); j++) u += ub[j];
					if (lb[i] < lb(u,i,j))
						changed = (lb[i] = lb(u,i,j)) == lb[i];
					for (j =   0; j < i; j++)                      l += lb[j];
					for (j = n-1; j > i && lb[j] > ub(l,i,j); j--) l += lb[j];
					if (ub[i] > ub(l,i,j))
						changed = (ub[i] = ub(l,i,j)) == ub[i];
				}
			}
		}
		
		private int lb(int upper, int i, int j)
		{
			return (int)Math.ceil((100d-upper)/(i-j+1));
		}
		
		private int ub(int lower, int i, int j)
		{
			return (int)Math.floor((100d-lower)/(j-i+1));
		}
		
		public int find(String needle)
		{
			for (int i = 0; i < n; i++)
				if (ingredient[i].equals(needle))
					return i;
			return -1;
		}
	}
	
}
